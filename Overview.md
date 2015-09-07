jefsr (Java EncFS Reader) is a library intended for accessing data stored in
EncFS volumes. From user perspective, the main difference between jefsr and
EncFS itself is that jefsr reads the data directly from encrypted files without
the need for mounting the file system. Additionally, jefsr permits only for
reading the data; there is no 'write' functionality.


The library is written in Java (tested against version 6) and depends on the following libraries:
  * XStream (tested against 1.4.2)
  * Base64 (2.3.7 version bundled in src/net/iharder/base64 directory)

jefsr was tested against EncFS 1.7.4 and no actions were taken to achieve
compatibility with old versions of EncFS. It is assumed that all configuration
options of EncFS 1.7.4 are supported.

In order to access files one must provide path to volume config file and user
password. The password is not stored anywhere and is used exclusively to
decipher master volume key. Currently only .encfs6.xml config files are
supported.

For usage tutorial check [GettingStarted](GettingStarted.md) section.