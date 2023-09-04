FROM amazonlinux as builder

ARG ARCHITECTURE=x64
ARG GRAALVM_VERSION=20.0.2

RUN yum update && yum -y install tar && yum -y install gzip &&  \
    yum -y install findutils && yum -y install gcc &&  \
    yum -y install gcc && yum -y install zlib*
RUN curl -o graalvm.tar.gz -LJO https://github.com/graalvm/graalvm-ce-builds/releases/download/jdk-${GRAALVM_VERSION}/graalvm-community-jdk-${GRAALVM_VERSION}_linux-${ARCHITECTURE}_bin.tar.gz &&  \
    tar -xzf graalvm.tar.gz
RUN mv graalvm-community-openjdk-20.0.2+9.1  /usr/local/graalvm-jdk-20

#RUN curl https://download.oracle.com/graalvm/20/latest/graalvm-jdk-20_linux-x64_bin.tar.gz | tar -xz
#RUN mv graalvm-jdk-20.0.2+9.1  /usr/local/graalvm-jdk-20

ENV JAVA_HOME=/usr/local/graalvm-jdk-20
ENV PATH=$PATH:$JAVA_HOME/bin

WORKDIR /app
COPY . ./
RUN ./gradlew nativeCompile -x test

FROM amazonlinux
WORKDIR service
COPY --from=builder /app/build/native/nativeCompile/demo /service/app
ENTRYPOINT ./app