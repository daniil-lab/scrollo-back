FROM gradle:7.3-jdk17 as build

RUN apt-get update

RUN apt-get install git

RUN git clone https://daniil-lab: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@github.com/daniil-lab/scrollo-back.git wp

WORKDIR wp

RUN gradle clean build

FROM openjdk:17-alpine

COPY --from=build /home/gradle/wp/build/libs/base-dev.jar .

EXPOSE 8080

ENTRYPOINT java -jar base-dev.jar

