package com.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpUtil {
	
	public static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

	public String charsetRemote = "GBK";
	public String charsetLocal = "iso-8859-1";    

	public FTPClient ftpClient = new FTPClient();

	public FtpUtil() {
		// 设置将过程中使用到的命令输出到控制台
		this.ftpClient.addProtocolCommandListener(new PrintCommandListener(
				new PrintWriter(System.out)));
	}   
   
	/** */
	/**
	 * 连接到FTP服务器
	 * 
	 * @param hostname
	 *            主机名
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 是否连接成功
	 * @throws IOException
	 */
	public boolean connect(String hostname, int port, String username,
			String password) throws IOException {
		logger.debug(hostname + "|" + port + "|" + username + "|" + password);
		ftpClient.setDefaultTimeout(60000);
		logger.debug("Ftp time out:" + ftpClient.getDefaultTimeout());
		ftpClient.connect(hostname, port);

		// 设置data read time out
		ftpClient.setDataTimeout(60000);
		ftpClient.setSoTimeout(60000);
		logger.debug("Connectted");
		ftpClient.setControlEncoding("GBK");
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			logger.debug("login ....");
			if (ftpClient.login(username, password)) {
				logger.debug("OK");
				return true;
			}
		}
		logger.debug("Fail");
		disconnect();
		return false;
	}

	/** */
	/**
	 * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报
	 * 
	 * @param remote
	 *            远程文件路径
	 * @param local
	 *            本地文件路径
	 * @return 上传的状态
	 * @throws Exception
	 */
	public DownloadStatus download(String remote, String local)
			throws Exception {
		FileOutputStream out = null;
		InputStream in = null;
		try {
			// 设置被动模式
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			DownloadStatus result;

			// 检查远程文件是否存在
			FTPFile[] files = ftpClient.listFiles(new String(remote
					.getBytes(charsetRemote), charsetLocal));
			if (files.length != 1) {
				logger.info("The remote file does not exist");
				return DownloadStatus.Remote_File_Noexist;
			}
			long lRemoteSize = files[0].getSize();
			File f = new File(local);
			SuperFile.deleteFile(local);
			// 本地存在文件，进行断点下载
			if (f.exists()) {
				long localSize = f.length();
				// 判断本地文件大小是否大于远程文件大小
				if (localSize > lRemoteSize) {
					logger.debug("Local file than remote file, download suspended");
					return DownloadStatus.Local_Bigger_Remote;
				}

				// 进行断点续传，并记录状态
				out = new FileOutputStream(f, true);
				ftpClient.setRestartOffset(localSize);
				in = ftpClient.retrieveFileStream(new String(remote
						.getBytes(charsetRemote), charsetLocal));
				byte[] bytes = new byte[1024];
				long step = lRemoteSize / 100;
				long process = localSize / step;
				int c;

				while ((c = in.read(bytes)) != -1) {
					out.write(bytes, 0, c);
					localSize += c;
					long nowProcess = localSize / step;
					if (nowProcess > process) {
						process = nowProcess;
						if (process % 10 == 0)
							logger.info("Download progress:" + process + "%");
					}
				}
				out.close();
				in.close();
				Thread.sleep(2000);
				logger.info("local file length:" + f.length()
						+ ", remote file lenght:" + lRemoteSize);

					result = DownloadStatus.Download_New_Success;
			} else {
				long startTime = System.currentTimeMillis();// 获取开始时间

				out = new FileOutputStream(f);
				in = ftpClient.retrieveFileStream(new String(remote
						.getBytes(charsetRemote), charsetLocal));
				byte[] bytes = new byte[1024];
				long step = lRemoteSize / 100;
				if (step == 0)
					step = 1;
				long process = 0;
				long localSize = 0L;
				int c;
				while ((c = in.read(bytes)) != -1) {
					out.write(bytes, 0, c);
					localSize += c;
					long nowProcess = localSize / step;
					if (nowProcess > process) {
						process = nowProcess;
						if (process % 10 == 0) {
							long endTime = System.currentTimeMillis();// 获取结束时间
							long elapsedTime = (endTime - startTime) / 1000;
							logger.info("Download progress:"
									+ process
									+ "%"
									+ " speed: "
									+ (elapsedTime == 0 ? 0
											: (localSize / 1024) / elapsedTime)
									+ "KB/s" + " time:" + elapsedTime + "s");
						}
					}
				}
				out.close();
				in.close();
				Thread.sleep(2000);

				logger.info("local file length:" + f.length()
						+ ", remote file lenght:" + lRemoteSize);
			
				result = DownloadStatus.Download_New_Success;
			}
			return result;
		} catch (Exception e) {
			throw new CloudPlatformRuntimeException("download file fail, localfile:" + local + ", remotefile:" + remote, e);
		} finally {
			try {
				disconnect();
			} catch (Exception ex) {
				logger.error("ftp disconnect faild", ex);
			}

			if (out != null)
				out.close();
			if (in != null)
				in.close();
		}
	}

	/**
	 * 上传文件到FTP服务器，支持断点续传
	 * 
	 * @param local
	 *            本地文件名称，绝对路径
	 * @param remote
	 *            远程文件路径，使用/home/directory1/subdirectory/file.ext或是
	 *            http://www.guihua.org /subdirectory/file.ext
	 *            按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
	 * @return 上传结果
	 * @throws IOException
	 */
	public UploadStatus upload(String local, String remote) throws Exception {
		try {
			// 设置PassiveMode传输
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制流的方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setControlEncoding("GBK");
			UploadStatus result;
			// 对远程目录的处理
			String remoteFileName = remote;
			if (remote.contains("/")) {
				remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
				// 创建服务器远程目录结构，创建失败直接返回
				if (CreateDirecroty(remote, ftpClient) == UploadStatus.Create_Directory_Fail) {
					logger.info("create remote directory fail");
					return UploadStatus.Create_Directory_Fail;
				}
				logger.info("create remote directory ok");
			}

			// 检查远程是否存在文件
			FTPFile[] files = ftpClient.listFiles(new String(remoteFileName
					.getBytes(charsetRemote), charsetLocal));
			if (files.length == 1) {
				long remoteSize = files[0].getSize();
				File f = new File(local);
				if (!f.exists()) {
					logger.debug("The local file=>" + local + " does not exist");
					return UploadStatus.Upload_New_File_Failed;
				}
				long localSize = f.length();

				logger.debug("local file name=>"
						+ SuperFile.getFileFullName(f.getPath()));
				logger.debug("remote file name=>" + files[0].getName());
				logger.debug("local file size=>" + localSize);
				logger.debug("remote file size=>" + remoteSize);
				if (SuperFile.getFileFullName(f.getPath()).equalsIgnoreCase(
						files[0].getName())
						&& (remoteSize == localSize)) {
					logger.debug("The remote file=>" + remoteFileName
							+ " has exist");
					return UploadStatus.Upload_New_File_Success;
				} else {
					boolean isDeleted = true;
					int retryTimes = 0;
					do {
						logger.info("delete remote file=>" + remoteFileName);
						isDeleted = ftpClient.deleteFile(remoteFileName);
						if (isDeleted) {
							logger.debug("ok");
							break;
						}
						logger.warn("cannot delete remote file=>"
								+ remoteFileName + ", retry");
						if (retryTimes++ > 5)
							break;
						Thread.sleep(3000);
					} while (!isDeleted);
				}
			}
			result = uploadFile(remoteFileName, new File(local), ftpClient, 0);
			return result;
		} catch (Exception e) {
			throw new CloudPlatformRuntimeException("upload file fail, localfile:" + local + ", remotefile:" + remote, e);
		} finally {
			try {
				disconnect();
			} catch (Exception ex) {
				logger.error("ftp disconnect faild", ex);
			}
		}
	}

	/** */
	/**
	 * 断开与远程服务器的连接
	 * 
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	/** */
	/**
	 * 递归创建远程服务器目录
	 * 
	 * @param remote
	 *            远程服务器文件绝对路径
	 * @param ftpClient
	 *            FTPClient对象
	 * @return 目录创建是否成功
	 * @throws IOException
	 */
	public UploadStatus CreateDirecroty(String remote, FTPClient ftpClient)
			throws IOException {
		UploadStatus status = UploadStatus.Create_Directory_Success;
		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		if (!directory.equalsIgnoreCase("/")
				&& !ftpClient.changeWorkingDirectory(new String(directory
						.getBytes(charsetRemote), charsetLocal))) {
			// 如果远程目录不存在，则递归创建远程服务器目录
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			while (true) {
				String subDirectory = new String(remote.substring(start, end)
						.getBytes(charsetRemote), charsetLocal);
				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						return UploadStatus.Create_Directory_Fail;
					}
				}

				start = end + 1;
				end = directory.indexOf("/", start);

				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return status;
	}

	/** */
	/**
	 * 上传文件到服务器,新上传和断点续传
	 * 
	 * @param remoteFile
	 *            远程文件名，在上传之前已经将服务器工作目录做了改变
	 * @param localFile
	 *            本地文件File句柄，绝对路径
	 * @param processStep
	 *            需要显示的处理进度步进值
	 * @param ftpClient
	 *            FTPClient引用
	 * @return
	 * @throws IOException
	 */
	public UploadStatus uploadFile(String remoteFile, File localFile,
			FTPClient ftpClient, long remoteSize) {
		RandomAccessFile raf = null;
		OutputStream out = null;
		try {
			logger.info("start upload file:" + remoteFile + "|" + remoteSize);
			UploadStatus status;
			// 显示进度的上传
			long step = localFile.length() / 100;
			if (step == 0)
				step = 1;
			long process = 0;
			long localreadbytes = 0L;
			raf = new RandomAccessFile(localFile, "r");
			out = ftpClient.appendFileStream(new String(remoteFile
					.getBytes(charsetRemote), charsetLocal));
			// 断点续传
			if (remoteSize > 0) {
				ftpClient.setRestartOffset(remoteSize);
				process = remoteSize / step;
				raf.seek(remoteSize);
				localreadbytes = remoteSize;
			}
			byte[] bytes = new byte[1024];
			int c;
			while ((c = raf.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localreadbytes += c;
				if (localreadbytes / step != process) {
					process = localreadbytes / step;
					logger.info("Upload progress:" + process + "%");
				}
			}
			out.flush();
			raf.close();
			out.close();
			Thread.sleep(2000);
			boolean result = ftpClient.completePendingCommand();
			if (remoteSize > 0) {
				status = result ? UploadStatus.Upload_From_Break_Success
						: UploadStatus.Upload_From_Break_Failed;
			} else {
				status = result ? UploadStatus.Upload_New_File_Success
						: UploadStatus.Upload_New_File_Failed;
			}
			return status;
		} catch (Exception e) {
			throw new CloudPlatformRuntimeException("upload file fail, localfile:" + localFile + ", remotefile:" + remoteFile, e);
		} finally {
			if (raf != null)
				try {
					raf.close();
				} catch (IOException ignore) {
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException ignore) {
				}
			try {
				disconnect();
			} catch (Exception ex) {
				logger.error("ftp disconnect faild", ex);
			}
		}
	}
}
