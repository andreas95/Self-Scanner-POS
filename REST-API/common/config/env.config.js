module.exports = {
    "port": process.env.PORT || 3000,
    "appEndpoint": "https://self-scanner-pos.herokuapp.com",
    "apiEndpoint": "https://self-scanner-pos.herokuapp.com",
    "jwt_secret": "andreasAPI",
    "jwt_expiration_in_seconds": 36000,
    "environment": "dev",
    "permissionLevels": {
        "CASHIER": 1,
        "MANAGER": 5,
        "ADMIN": 10
    }
};
