This subdirectory stores data used for unit and integration tests included in
the library.

config_files directory holds configuration files for various types of FS.
Names of config_files subdirectories illustrate FS configuration

create_test_fs.sh script generates filesystems and seeds them with data stored
in plaintext_data directory. If you want to add new test files which should be
used in Volume integration testing, put the files somewhere in plaintext_data
directory tree and call create_test_fs.sh script. Afterwards, you are free to
run JUnit.

The password for all of the FS *must* be "test" (without quotes.) If a
filesystem won't meet this requirement it will cause false failures during
integration tests.
