SUMMARY = "EFI Application used by uefi-capsule plugin in fwupd"
HOMEPAGE = "https://fwupd.org"
LICENSE = "LGPL-2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

SRC_URI = "git://github.com/fwupd/fwupd-efi.git;branch=main"
SRCREV = "e8f4ced93641c3deb843a54683473979afc205ab"

S = "${WORKDIR}/git"

inherit meson
