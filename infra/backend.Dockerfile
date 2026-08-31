# ==============================================================================
#  Backend — Spring Boot 4 / Java 17, empacotado como WAR executável.
#
#  Build em 2 estágios:
#    1) maven  → resolve dependências e gera target/fintech_app-0.0.1-SNAPSHOT.war
#    2) jre    → imagem final, só o runtime + o artefato (~200MB em vez de ~800MB)
#
#  O pom usa <packaging>war</packaging> com spring-boot-starter-tomcat em escopo
#  "provided". O spring-boot-maven-plugin reempacota isso num WAR EXECUTÁVEL
#  (o Tomcat vai para WEB-INF/lib-provided), então `java -jar app.war` funciona
#  igual a um jar comum — não precisa de servidor de aplicação externo.
#
#  Contexto de build: backend/fintech_app
# ==============================================================================

# ── Estágio 1: build ──────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Copiar só o pom primeiro faz o Docker cachear o download das dependências:
# enquanto o pom.xml não mudar, esta camada é reaproveitada e o build é rápido.
COPY pom.xml .
# O `|| true` é intencional: go-offline é só aquecimento de cache. Se algum
# plugin não resolver aqui, o `package` abaixo baixa o que faltar e falha de
# verdade se for problema real — não vale abortar o build por causa do cache.
RUN mvn -B -q dependency:go-offline || true

COPY src ./src
COPY checkstyle.xml .
RUN mvn -B -DskipTests package

# ── Estágio 2: runtime ────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre
WORKDIR /app

# Usuário não-root: se alguém escapar da aplicação, não escapa como root.
RUN groupadd --system spring && useradd --system --gid spring spring

COPY --from=build /build/target/*.war app.war

# Diretório de uploads (APP_UPLOAD_DIR aponta para cá; é um volume no compose).
RUN mkdir -p /app/uploads/extratos && chown -R spring:spring /app

USER spring
EXPOSE 8082

# -XX:MaxRAMPercentage faz a JVM respeitar o limite de memória do container
# em vez de enxergar a RAM inteira da máquina host.
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.war"]
