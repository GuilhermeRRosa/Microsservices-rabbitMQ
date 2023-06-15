FROM gradle:7.6.1-jdk11
COPY --chown=gradle:gradle . .
WORKDIR .
RUN gradle build -x test --no-daemon
EXPOSE 8082
CMD ["gradle", "bootRun"]