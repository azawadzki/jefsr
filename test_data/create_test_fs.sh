#!/bin/bash

TEST_FS_DIR=test_fs
DATA_SRC=plaintext_data
MOUNT_DIR=tmp_mount
PSWD=test
CONFIG_FILES_DIR=config_files

rm -rf ${TEST_FS_DIR} ${MOUNT_DIR}
mkdir ${TEST_FS_DIR} ${MOUNT_DIR}

for fs_name in $(ls ${CONFIG_FILES_DIR}); do
    curr_fs_dir=${TEST_FS_DIR}/${fs_name}
    mkdir ${curr_fs_dir}
    cp ${CONFIG_FILES_DIR}/${fs_name}/.encfs6.xml ${curr_fs_dir}
    echo "${PSWD}" | encfs -S $(readlink -f ${curr_fs_dir}) $(readlink -f ${MOUNT_DIR})
    cp -a ${DATA_SRC}/* ${MOUNT_DIR}
    fusermount -u ${MOUNT_DIR}
done

rm -rf ${MOUNT_DIR}
