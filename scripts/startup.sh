mvn clean install

unset GOOGLE_GENAI_USE_VERTEXAI
unset GOOGLE_API_KEY
unset CREATE_TICKET_SCHEME
unset CREATE_TICKET_HOST
unset CREATE_TICKET_PORT

echo "✅ Environment variables have been cleared."
echo "GOOGLE_GENAI_USE_VERTEXAI: $GOOGLE_GENAI_USE_VERTEXAI"
echo "GOOGLE_API_KEY: $GOOGLE_API_KEY"
echo "CREATE_TICKET_SCHEME: $CREATE_TICKET_SCHEME"
echo "CREATE_TICKET_HOST: $CREATE_TICKET_HOST"
echo "CREATE_TICKET_PORT: $CREATE_TICKET_PORT"

export GOOGLE_GENAI_USE_VERTEXAI=FALSE
export GOOGLE_API_KEY=
export CREATE_TICKET_SCHEME=http
export CREATE_TICKET_HOST=localhost
export CREATE_TICKET_PORT=8081

echo "✅ Environment variables have been set."
echo "GOOGLE_GENAI_USE_VERTEXAI: $GOOGLE_GENAI_USE_VERTEXAI"
echo "GOOGLE_API_KEY: $GOOGLE_API_KEY"
echo "CREATE_TICKET_SCHEME: $CREATE_TICKET_SCHEME"
echo "CREATE_TICKET_HOST: $CREATE_TICKET_HOST"
echo "CREATE_TICKET_PORT: $CREATE_TICKET_PORT"

mvn exec:java \
  -Dserver.port=9090 -Dexec.mainClass="com.google.adk.web.AdkWebServer" \
  -Dexec.args="--adk.agents.source-dir=src/main/java" \
  -Dexec.classpathScope="compile"
