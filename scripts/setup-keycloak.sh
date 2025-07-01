#!/bin/bash

# Mankind Matrix AI Backend - Automated Keycloak Setup Script
# This script downloads, sets up, and configures Keycloak automatically

# Load environment variables from .env
if [ -f "../../.env" ]; then
    echo "🔄 Loading environment variables from .env..."
    set -a
    source ../../.env
    set +a
elif [ -f "../.env" ]; then
    echo "🔄 Loading environment variables from .env..."
    set -a
    source ../.env
    set +a
elif [ -f ".env" ]; then
    echo "🔄 Loading environment variables from .env..."
    set -a
    source .env
    set +a
else
    echo "⚠️  .env file not found! Database config will not be set."
fi

# Set Keycloak DB config from .env if available
if [ ! -z "$DB_HOST" ]; then
    export KC_DB=mysql
    export KC_DB_URL="jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&autoReconnect=true"
    export KC_DB_USERNAME=$DB_USERNAME
    export KC_DB_PASSWORD=$DB_PASSWORD
    echo "📊 Using MySQL DB: $DB_HOST/$DB_NAME"
fi

# Set Keycloak admin credentials from .env if available
if [ ! -z "$ADMIN_USERNAME" ]; then
    export KC_BOOTSTRAP_ADMIN_USERNAME=$ADMIN_USERNAME
    export KC_BOOTSTRAP_ADMIN_PASSWORD=$ADMIN_PASSWORD
    echo "👤 Using admin credentials: $ADMIN_USERNAME"
else
    # Fallback to default admin credentials
    export KC_BOOTSTRAP_ADMIN_USERNAME=admin
    export KC_BOOTSTRAP_ADMIN_PASSWORD=admin
    echo "👤 Using default admin credentials: admin/admin"
fi

echo "🔐 Setting up Keycloak for Mankind Matrix AI Backend (Automated)..."

# Check if Keycloak is already installed
if [ -d "keycloak-26.0.5" ]; then
    echo "✅ Keycloak is already installed in keycloak-26.0.5/"
    echo "   Starting Keycloak with automated setup..."
else
    # Download Keycloak
    echo "📥 Downloading Keycloak 26.0.5..."
    curl -L https://github.com/keycloak/keycloak/releases/download/26.0.5/keycloak-26.0.5.tar.gz -o keycloak.tar.gz

    # Extract Keycloak
    echo "📦 Extracting Keycloak..."
    tar -xzf keycloak.tar.gz

    # Create configuration directory
    mkdir -p keycloak-26.0.5/conf

    # Create configuration file with admin user
    echo "⚙️ Creating Keycloak configuration..."
    cat > keycloak-26.0.5/conf/keycloak.conf << EOF
# Keycloak Configuration
http-port=${KEYCLOAK_HTTP_PORT:-8180}
http-enabled=true
import-realm=true
admin=${ADMIN_USERNAME:-admin}
admin-password=${ADMIN_PASSWORD:-admin}
EOF

    # Copy realm configuration
    echo "📋 Copying realm configuration..."
    mkdir -p keycloak-26.0.5/data/import
    cp keycloak/mankind-realm.json keycloak-26.0.5/data/import/

    # Clean up download
    rm keycloak.tar.gz
fi

# Start Keycloak in the background
echo "🚀 Starting Keycloak with automated setup..."
cd keycloak-26.0.5
./bin/kc.sh start-dev --import-realm

# Wait for Keycloak to be ready
echo "⏳ Waiting for Keycloak to start..."
sleep 15

# Check if Keycloak is ready
echo "🔍 Checking if Keycloak is ready..."
for i in {1..30}; do
    if curl -s http://localhost:8180/health/ready > /dev/null 2>&1; then
        echo "✅ Keycloak is ready!"
        break
    fi
    echo "   Waiting... (attempt $i/30)"
    sleep 2
done

# Verify admin user and realm
echo "🔐 Verifying setup..."
if curl -s http://localhost:8180/admin > /dev/null 2>&1; then
    echo "✅ Keycloak admin console is accessible"
else
    echo "❌ Keycloak admin console not accessible"
fi

echo ""
echo "🎉 Keycloak setup complete!"
echo ""
echo "📋 Access Information:"
echo "   Keycloak Admin:   http://localhost:8180/admin"
echo "   Admin Username:   admin"
echo "   Admin Password:   admin"
echo "   Realm:            mankind (automatically imported)"
echo "   Client Secret:    EbdR3is1KVHnCHGvqcN2WJzCJmGSp0m535rdRjBwE6U="
echo ""
echo "💡 You can now run: ./scripts/run-all-services.sh" 