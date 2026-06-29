FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /build

COPY backend/pom.xml ./pom.xml
RUN mvn -B -DskipTests dependency:go-offline

COPY backend/src ./src
RUN mvn -B -DskipTests package


FROM rockylinux:9

WORKDIR /app

ARG INSTALL_BIO_TOOLS=false
ARG INSTALL_TREE_RENDERING_DEPS=true

RUN dnf install -y \
        java-17-openjdk-headless \
        python3 \
        python3-pip \
        perl \
        tar \
        gzip \
        glib2 \
        mesa-libGL \
        libXrender \
        libXext \
        libSM \
        libxcb \
        fontconfig \
        dejavu-sans-fonts \
    && dnf clean all \
    && rm -rf /var/cache/dnf

# The application can be deployed before the large bioinformatics packages
# arrive. Set INSTALL_BIO_TOOLS=true after all three packages are available.
COPY bio_software/ /tmp/bio_software/
RUN if [ "${INSTALL_BIO_TOOLS}" = "true" ]; then \
        test -f /tmp/bio_software/mafft-7.526-gcc_fc6.x86_64.rpm \
        && test -f /tmp/bio_software/ncbi-blast-2.15.0+-x64-linux.tar.gz \
        && test -f /tmp/bio_software/iqtree-3.1.2-Linux-intel.tar.gz \
        && dnf install -y /tmp/bio_software/mafft-7.526-gcc_fc6.x86_64.rpm \
        && mkdir -p /opt/bio/blast /opt/bio/iqtree \
        && tar -xzf /tmp/bio_software/ncbi-blast-2.15.0+-x64-linux.tar.gz \
            -C /opt/bio/blast --strip-components=1 \
        && tar -xzf /tmp/bio_software/iqtree-3.1.2-Linux-intel.tar.gz \
            -C /opt/bio/iqtree --strip-components=1 \
        && ln -s /opt/bio/blast/bin/blastn /usr/local/bin/blastn \
        && ln -s /opt/bio/blast/bin/makeblastdb /usr/local/bin/makeblastdb \
        && ln -s /opt/bio/blast/bin/blastdbcmd /usr/local/bin/blastdbcmd \
        && ln -s /opt/bio/iqtree/bin/iqtree3 /usr/local/bin/iqtree3; \
    else \
        echo "Skipping BLAST, MAFFT and IQ-TREE installation"; \
    fi \
    && dnf clean all \
    && rm -rf /var/cache/dnf /tmp/bio_software

# Tree rendering can be installed independently of BLAST, MAFFT and IQ-TREE.
RUN if [ "${INSTALL_TREE_RENDERING_DEPS}" = "true" ]; then \
        python3 -m pip install --no-cache-dir --timeout 120 --retries 10 \
            --index-url https://mirrors.aliyun.com/pypi/simple/ \
            --trusted-host mirrors.aliyun.com \
            ete3==3.1.3 \
            PyQt5==5.15.11 \
            six==1.17.0 \
            numpy==1.26.4; \
    else \
        echo "Skipping ETE3/PyQt5 tree-rendering dependencies"; \
    fi

ENV QT_QPA_PLATFORM=offscreen

COPY --from=build /build/target/*.jar /app/app.jar

RUN mkdir -p \
        /app/files \
        /app/templates \
        /app/scripts \
        /app/bio-data \
        /app/bio-jobs \
    && java -version \
    && if [ "${INSTALL_TREE_RENDERING_DEPS}" = "true" ]; then \
        python3 -c "from ete3 import Tree; from PyQt5 import QtCore; print('ETE3/PyQt5 OK')"; \
    fi \
    && if [ "${INSTALL_BIO_TOOLS}" = "true" ]; then \
        blastn -version \
        && makeblastdb -version \
        && blastdbcmd -version \
        && mafft --version \
        && iqtree3 -version; \
    fi

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
