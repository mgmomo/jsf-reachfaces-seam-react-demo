#!/bin/bash
# Build the React frontend and Java WAR, then deploy to local JBoss
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$SCRIPT_DIR"
JBOSS_HOME="$SCRIPT_DIR/local/jboss-as-7.1.1.Final"

echo "=== Building React frontend ==="
cd "$PROJECT_DIR/frontend"
npm run build

echo ""
echo "=== Building WAR ==="
cd "$PROJECT_DIR"
mvn clean package -q

echo ""
echo "=== Deploying to JBoss ==="
cp "$PROJECT_DIR/target/wision4-seam.war" "$JBOSS_HOME/standalone/deployments/"

echo ""
echo "Waiting for deployment..."
for i in $(seq 1 30); do
    if [ -f "$JBOSS_HOME/standalone/deployments/wision4-seam.war.deployed" ]; then
        echo "Deployed successfully."
        exit 0
    fi
    if [ -f "$JBOSS_HOME/standalone/deployments/wision4-seam.war.failed" ]; then
        echo "ERROR: Deployment failed. Check server log:"
        echo "  $JBOSS_HOME/standalone/log/server.log"
        exit 1
    fi
    sleep 1
done

echo "WARNING: Deployment status unknown after 30s. Check server log."
