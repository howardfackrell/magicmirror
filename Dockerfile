FROM quay.octanner.io/base/oct-scala:2.12.4-sbt-0.13.16-play-2.6.9

COPY . .

RUN sbt clean stage

EXPOSE 9000

CMD [ "./start.sh" ]
