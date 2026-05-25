# ---- Build ----
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace

COPY mvnw .
COPY .mvn/ .mvn/

# POMs separados para aproveitar cache de dependências
COPY pom.xml .
COPY aplicacao/pom.xml aplicacao/
COPY apresentacao-backend/pom.xml apresentacao-backend/
COPY apresentacao-frontend/pom.xml apresentacao-frontend/
COPY dominio-compartilhado/pom.xml dominio-compartilhado/
COPY dominio-academico/pom.xml dominio-academico/
COPY dominio-avaliacao/pom.xml dominio-avaliacao/
COPY dominio-usuarios/pom.xml dominio-usuarios/
COPY infraestrutura/pom.xml infraestrutura/
COPY bdd/acadtrackbdd/pom.xml bdd/acadtrackbdd/

RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
RUN ./mvnw dependency:go-offline -B -q

COPY . .
RUN ./mvnw package -DskipTests -B -q -pl apresentacao-backend -am

# ---- Run ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /workspace/apresentacao-backend/target/apresentacao-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
