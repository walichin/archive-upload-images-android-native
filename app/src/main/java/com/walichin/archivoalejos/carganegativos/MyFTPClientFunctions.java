package com.walichin.archivoalejos.carganegativos;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MyFTPClientFunctions {

    //public static String FTP_CURR_DIR;
    private static final String TAG = "MyFTPClientFunctions";
	public FTPClient mFTPClient = null;

	// Method to connect to FTP server:
	public boolean ftpConnect(String host, String username, String password, int port) {

        boolean status = false;

		try {
			mFTPClient = new FTPClient();

			// connecting to the host
			mFTPClient.connect(host, port);

			// now check the reply code, if positive mean connection success
			if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {

				// login using username & password
                status = mFTPClient.login(username, password);

				if (status) {

                    /*
                     * Set File Transfer Mode
                     *
                     * To avoid corruption issue you must specified a correct
                     * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
                     * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
                     * transferring text, image, and compressed files.
                     */
                    mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                    mFTPClient.enterLocalPassiveMode();

                }
			}
		} catch (Exception e) {
			Log.e(TAG, "Error: could not connect to host " + host + " " + e.getMessage());
		}
		return status;
	}

	// Method to disconnect from FTP server:

	public boolean ftpDisconnect() {
		try {
			mFTPClient.logout();
			mFTPClient.disconnect();
			return true;
		} catch (Exception e) {
			Log.d(TAG, "Error occurred while disconnecting from ftp server.");
		}

		return false;
	}

	// Method to get current working directory:

	public String ftpGetCurrentWorkingDirectory() {
		try {
			String workingDir = mFTPClient.printWorkingDirectory();
			return workingDir;
		} catch (Exception e) {
			Log.d(TAG, "Error: could not get current working directory.");
		}

		return null;
	}

	// Method to change working directory:

	public boolean ftpChangeDirectory(String directory_path) {
		try {
			boolean result = mFTPClient.changeWorkingDirectory(directory_path);

            if (result){
                return true;
            } else {
                Log.d(TAG, "Error: could not change directory to " + directory_path);
                return false;
            }
		} catch (Exception e) {

			Log.d(TAG, "Error: could not change directory to " + directory_path);
            Log.e(TAG, "Error: " + e.getMessage());
            return false;
		}
	}

	// Method to list all files in a directory:

	public String[] ftpPrintFilesList(String dir_path) {
		String[] fileList = null;
		try {
			FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
			int length = ftpFiles.length;
			fileList = new String[length];
			for (int i = 0; i < length; i++) {
				String name = ftpFiles[i].getName();
				boolean isFile = ftpFiles[i].isFile();

				if (isFile) {
					fileList[i] = "File :: " + name;
					Log.i(TAG, "File : " + name);
				} else {
					fileList[i] = "Directory :: " + name;
					Log.i(TAG, "Directory : " + name);
				}
			}
			return fileList;
		} catch (Exception e) {
			e.printStackTrace();
			return fileList;
		}
	}

	// Method to create new directory:

	public boolean ftpMakeDirectory(String new_dir_path) {
		try {
			boolean status = mFTPClient.makeDirectory(new_dir_path);
			return status;
		} catch (Exception e) {
			Log.d(TAG, "Error: could not create new directory named "
					+ new_dir_path);
		}

		return false;
	}

	// Method to delete/remove a directory:

	public boolean ftpRemoveDirectory(String dir_path) {
		try {
			boolean status = mFTPClient.removeDirectory(dir_path);
			return status;
		} catch (Exception e) {
			Log.d(TAG, "Error: could not remove directory named " + dir_path);
		}

		return false;
	}

	// Method to delete a file:

	public boolean ftpRemoveFile(String filePath) {
		try {
			boolean status = mFTPClient.deleteFile(filePath);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// Method to rename a file:

	public boolean ftpRenameFile(String from, String to) {
		try {
			boolean status = mFTPClient.rename(from, to);
			return status;
		} catch (Exception e) {
			Log.d(TAG, "Could not rename file: " + from + " to: " + to);
		}

		return false;
	}

	// Method to download a file from FTP server:

	/**
	 * mFTPClient: FTP client connection object (see FTP connection example)
	 * srcFilePath: path to the source file in FTP server desFilePath: path to
	 * the destination file to be saved in sdcard
	 */
	public boolean ftpDownload(String srcFilePath, String desFilePath) {
		boolean status = false;
		try {
			FileOutputStream desFileStream = new FileOutputStream(desFilePath);
			;
			status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
			desFileStream.close();

			return status;
		} catch (Exception e) {
			Log.d(TAG, "download failed");
		}

		return status;
	}

	// Method to upload a file to FTP server:

	/**
	 * mFTPClient: FTP client connection object (see FTP connection example)
	 * srcFilePath: source file path in sdcard desFileName: file name to be
	 * stored in FTP server desDirectory: directory path where the file should
	 * be upload to
	 */
	//public boolean ftpUpload(String srcFilePath, String desFileName,
	public boolean ftpUpload(ParcelFileDescriptor pfd, String desFileName,
							 String desDirectory, Context context) {

		boolean status = false;
		ParcelFileDescriptor mInputPFD;

		try {

			// Get a regular file descriptor for the file
			FileDescriptor fd = pfd.getFileDescriptor();

			//FileInputStream srcFileStream = new FileInputStream(srcFilePath);
            FileInputStream srcFileStream = new FileInputStream(fd);

            //FTP_CURR_DIR = mFTPClient.printWorkingDirectory();

			// change working directory to the destination directory
			if (ftpChangeDirectory(desDirectory)) {
                status = mFTPClient.storeFile(desFileName, srcFileStream);
			}

            //FTP_CURR_DIR = mFTPClient.printWorkingDirectory();

			srcFileStream.close();

			return status;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "upload failed: " + e);
		}

		return status;
	}
}