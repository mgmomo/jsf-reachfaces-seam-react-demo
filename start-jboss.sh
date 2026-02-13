#!/bin/bash
# Start JBoss AS 7.1.1 with the local Java 7 environment
# Port offset 100: application available at http://localhost:8180/wision4-seam/

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JBOSS_HOME="$SCRIPT_DIR/local/jboss-as-7.1.1.Final"

# Stop any running instance first
PIDS=$(pgrep -f "$JBOSS_HOME")
if [ -n "$PIDS" ]; then
    echo "Stopping existing JBoss instance..."
    kill $PIDS 2>/dev/null
    sleep 3
    # Force kill if still running
    PIDS=$(pgrep -f "$JBOSS_HOME")
    if [ -n "$PIDS" ]; then
        kill -9 $PIDS 2>/dev/null
        sleep 1
    fi
fi

# Clean deployment markers from failed/stuck deployments
rm -f "$JBOSS_HOME/standalone/deployments/"*.failed
rm -f "$JBOSS_HOME/standalone/deployments/"*.isdeploying

echo "Starting JBoss AS 7.1.1 (port offset 100)..."
echo "  JBOSS_HOME: $JBOSS_HOME"
echo "  URL: http://localhost:8180/wision4-seam/"
echo ""

exec "$JBOSS_HOME/bin/standalone.sh" -b 0.0.0.0 -Djboss.socket.binding.port-offset=100
