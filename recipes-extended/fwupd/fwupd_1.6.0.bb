SUMMARY = "A simple daemon to allow session software to update firmware"
HOMEPAGE = "https://fwupd.org"
LICENSE = "LGPL-2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = " \
    gcab \
    gcab-native \
    glib-2.0 \
    json-glib \
    libjcat \
    libsoup-2.4 \
    libxmlb \
"

SRC_URI = "git://github.com/fwupd/fwupd.git"
SRC_URI += "file://0001-Fix-shebang-for-cross-compile.patch"
SRC_URI += "file://0001-Define-gmodule-unconditionally.patch"
SRCREV = "ba890cde4621aca2b8d52a5862bcaea4b390a921"

S = "${WORKDIR}/git"

inherit bash-completion gobject-introspection gtk-doc gtk-icon-cache manpages meson python3native vala

GTKDOC_MESON_OPTION = "gtkdoc"

# Requires libsmbios_c which does not yet have a recipe.
EXTRA_OEMESON = "-Dplugin_dell=false"
# Requires pangocairo gobject introspection library
EXTRA_OEMESON += "-Dplugin_uefi_capsule_splash=false"
# OE projects probably don't care about QubesOS support
EXTRA_OEMESON += "-Dqubes=false"
# OE does not have elogind support or recipe
EXTRA_OEMESON += "-Delogind=false"
# Disable tests until ptest integration is complete
EXTRA_OEMESON += "-Dtests=false"
# Disable automatic checkout and build of fwupd-efi. This is built in a
# separate recipe.
EXTRA_OEMESON += "-Defi_binary=false"

# Keep PACKAGECONFIG options in the same order as upstream meson_options.txt
PACKAGECONFIG ??= " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'polkit systemd', d)} \
    build-all \
"

# Select one of all, standalone, or library. all and standalone require gusb
# to be enabled. library requires gusb to be disabled.
PACKAGECONFIG[build-all] = "-Dbuild=all -Dgusb=true,,udev libgusb"
PACKAGECONFIG[build-standalone] = "-Dbuild=standalone -Dgusb=true,,libgusb"
PACKAGECONFIG[build-library] = "-Dbuild=library -Dgusb=false"

PACKAGECONFIG[agent] = "-Dagent=true,-Dagent=false"
PACKAGECONFIG[consolekit] = "-Dconsolekit=true,-Dconsolekit=false"
PACKAGECONFIG[firmware-packager] = "-Dfirmware-packager=true,-Dfirmware-packager=false,,python3-core"
PACKAGECONFIG[lvfs] = "-Dlvfs=true,-Dlvfs=false"
PACKAGECONFIG[manpages] = "-Dman=true,-Dman=false"
PACKAGECONFIG[libarchive] = "-Dlibarchive=true,-Dlibarchive=false,libarchive"
PACKAGECONFIG[gudev] = "-Dgudev=true,-Dgudev=false,libgudev"
PACKAGECONFIG[bluez] = "-Dbluez=true,-Dbluez=false"
PACKAGECONFIG[polkit] = "-Dpolkit=true,-Dpolkit=false,polkit udev"
PACKAGECONFIG[gnutls] = "-Dgnutls=true,-Dgnutls=false,gnutls"
PACKAGECONFIG[lzma] = "-Dlzma=true,-Dlzma=false,xz"
PACKAGECONFIG[plugin_altos] = "-Dplugin_altos=true,-Dplugin_altos=false,elfutils"
PACKAGECONFIG[plugin_amt] = "-Dplugin_amt=true,-Dplugin_amt=false"
PACKAGECONFIG[plugin_dummy] = "-Dplugin_dummy=true,-Dplugin_dummy=false"
PACKAGECONFIG[plugin_emmc] = "-Dplugin_emmc=true,-Dplugin_emmc=false"
PACKAGECONFIG[plugin_synaptics_mst] = "-Dplugin_synaptics_mst=true,-Dplugin_synaptics_mst=false"
PACKAGECONFIG[plugin_synaptics_rmi] = "-Dplugin_synaptics_rmi=true,-Dplugin_synaptics_rmi=false"
PACKAGECONFIG[plugin_tpm] = "-Dplugin_tpm=true,-Dplugin_tpm=false,tpm2-tss"
PACKAGECONFIG[plugin_thunderbolt] = "-Dplugin_thunderbolt=true,-Dplugin_thunderbolt=false"
PACKAGECONFIG[plugin_redfish] = "-Dplugin_redfish=true,-Dplugin_redfish=false,efivar"
PACKAGECONFIG[plugin_uefi_capsule] = "-Dplugin_uefi_capsule=true,-Dplugin_uefi_capsule=false,efivar fwupd-efi"
PACKAGECONFIG[plugin_uefi_pk] = "-Dplugin_uefi_pk=true,-Dplugin_uefi_pk=false"
PACKAGECONFIG[plugin_nvme] = "-Dplugin_nvme=true,-Dplugin_nvme=false"
PACKAGECONFIG[plugin_modem_manager] = "-Dplugin_modem_manager=true,-Dplugin_modem_manager=false,modemmanager"
PACKAGECONFIG[plugin_msr] = "-Dplugin_msr=true,-Dplugin_msr=false"
PACKAGECONFIG[plugin_flashrom] = "-Dplugin_flashrom=true,-Dplugin_flashrom=false,flashrom"
PACKAGECONFIG[plugin_platform_integrity] = "-Dplugin_platform_integrity=true,-Dplugin_platform_integrity=false"
PACKAGECONFIG[plugin_intel_spi] = "-Dplugin_intel_spi=true,-Dplugin_intel_spi=false"
PACKAGECONFIG[supported_build] = "-Dsupported_build=true,-Dsupported_build=false"
PACKAGECONFIG[systemd] = "-Dsystemd=true,-Dsystemd=false"
PACKAGECONFIG[soup_session_compat] = "-Dsoup_session_compat=true,-Dsoup_session_compat=false"
PACKAGECONFIG[curl] = "-Dcurl=true,-Dcurl=false,curl"

do_install_append() {
    # Remove fish completion files until someone creates a fish-completion
    # class or something similar.
    rm -rf ${D}${datadir}/fish
}

FILES_${PN} += " \
    ${datadir}/dbus-1 \
    ${datadir}/metainfo \
    ${datadir}/polkit-1 \
    ${libdir}/fwupd-plugins-3 \
"
