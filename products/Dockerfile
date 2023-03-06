# This container does not build the actual program,
# it will just copy the binary from the build folder.
# That has been done, because the rust toolchain is
# relativley big and it would be inefficient for those
# that already have it installed (Github Actions and devs).

# Using ubuntu, because it has relatibley new version of glibc
# and because Github Actions also uses it.
FROM ubuntu:latest
COPY target/release/products /
CMD ["./products"]
