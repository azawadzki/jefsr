# Installation #
  * Download jefsr jar and add it to your build path. Make sure that XStream is also configured (no particular version needed, the library was only tested against XStream 1.4.2, though.)
  * Make sure you have Unlimited Strength Jurisdiction Policy Files for your JVM installed. If you omit this step, you won't be able to use keys longer than 128 bits. (Step normally not required on most Linux distributions.)
  * If you don't have standard SUN JCE provider installed, make sure you have some other crypto provider in place ([Bouncy Castle](http://www.bouncycastle.org/) is normally a good pick.)

# Using the library #
jefsr interface consists of a single class with just a couple of methods, so handling the library should be rather straightforward.

In order to read data from EncFS volume, jefsr needs to read in the contents of volume configuration file (only V6 version is currently supported, so you need to look for .encfs6.xml files). Afterwards you need to initialize the library with volume password:
```
Volume v = new Volume(pathToConfigFile);
v.init(userPassword);
```
Take note that `init()` call may take some time as it needs to generate your volume master key. Now you are ready to get the data. Filename and contents decryption can be done with the following calls
```

BufferedInputStream bufStream = new BufferedInputStream(FileInputStream(fileToDecrypt));
ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

v.decryptFile(fileToDecrypt, bufStream, byteOut);

System.out.printf("decrypted name: %s", v.decryptPath(intraVolumePath));
System.out.printf("decrypted contents size: %d", byteOut.toByteArray().length);
```
That's all! Remember to use file path relative to encrypted file system root. This is necessary due to the fact that EncFS normally uses the whole path of the file for decryption (not only its name.)