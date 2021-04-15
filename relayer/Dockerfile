FROM openjdk:14-alpine
ARG apiport=8080
COPY ./build/install/relayer/ /relayer/

# COPY ./build/relayer.config /relayer/bin
WORKDIR /relayer/bin
EXPOSE $apiport
ENTRYPOINT ["sh", "relayer", "PORT", "$apiport"]