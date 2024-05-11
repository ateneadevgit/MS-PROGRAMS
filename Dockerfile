FROM openjdk:17-oracle
RUN mkdir data-shared
COPY target/ms-program.jar ms-program.jar
EXPOSE 8016
ENTRYPOINT ["java","-jar","/ms-program.jar"]