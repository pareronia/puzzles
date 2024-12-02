ENCODED = """\
g fmnc wms bgblr rpylqjyrc gr zw fylb. rfyrq ufyr amknsrcpq ypc dmp. bmgle \
gr gl zw fylb gq glcddgagclr ylb rfyr'q ufw rfgq rcvr gq qm jmle. sqgle \
qrpgle.kyicrpylq() gq pcamkkclbcb. lmu ynnjw ml rfc spj."""
D = str.maketrans(
    "abcdefghijklmnopqrstuvwxyz.()", "cdefghijklmnopqrstuvwxyzab.()"
)
URL = "map"

print(ENCODED.translate(D))
print(URL.translate(D))
