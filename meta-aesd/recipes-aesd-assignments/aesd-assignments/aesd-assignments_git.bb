# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-BhaktiRamani.git;protocol=ssh;branch=main"

PV = "1.0+git${SRCPV}"
SRCREV = "442013d465900432e9ab49a3a171f41780c05edf"


# Enable the startup script - changes that I added after image build. 
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
#INITSCRIPT_NAME:${PN} = "aesdsocket-start-stop.sh"
INITSCRIPT_NAME:${PN} = "aesdsocket-start-stop"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
S = "${WORKDIR}/git/server"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
FILES:${PN} += "${bindir}/aesdsocket"
#FILES:${PN} += "${bindir}/aesdsocket-start-stop.sh"
FILES:${PN} += "${sysconfdir}/init.d/aesdsocket-start-stop"
# TODO: customize these as necessary for any libraries you need for your application
# (and remove comment)
TARGET_LDFLAGS += "-pthread -lrt"

do_configure () {
	:
}

do_compile () {
	oe_runmake
}


do_install () {

    install -d ${D}${bindir}
    install -m 0755 ${S}/aesdsocket ${D}${bindir}/

    # Install the init script
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/aesdsocket-start-stop.sh ${D}${sysconfdir}/init.d/aesdsocket-start-stop
}

# Add post-installation step
pkg_postinst_ontarget:${PN}() {
	# If you need to handle starting the service after installation, do so here
	echo "Started the aesdsocket"
	
}

