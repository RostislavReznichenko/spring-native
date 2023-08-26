FROM amazonlinux as builder

#RUN yum update && yum -y install zip && yum -y install unzip
RUN yum update && yum -y install tar && yum -y install gzip &&  \
    yum -y install findutils && yum -y install gcc &&  \
    yum -y install gcc && yum -y install zlib*
RUN curl https://download.oracle.com/graalvm/20/latest/graalvm-jdk-20_linux-x64_bin.tar.gz | tar -xz

RUN mv graalvm-jdk-20.0.2+9.1  /usr/local/graalvm-jdk-20
ENV JAVA_HOME=/usr/local/graalvm-jdk-20
ENV PATH=$PATH:$JAVA_HOME/bin

WORKDIR /app
COPY . ./
RUN ./gradlew nativeCompile -x test

FROM amazonlinux
WORKDIR service
COPY --from=builder /app/build/native/nativeCompile/demo /service/app
EXPOSE 8081
ENTRYPOINT ./app