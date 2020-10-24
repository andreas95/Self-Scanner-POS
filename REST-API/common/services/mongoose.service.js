const mongoose = require('mongoose');
const crypto = require('crypto');
let count = 0;

const options = {
    autoIndex: false, // Don't build indexes
    poolSize: 10, // Maintain up to 10 socket connections
    // If not connected, return errors immediately rather than waiting for reconnect
    bufferMaxEntries: 0,
    // all other approaches are now deprecated by MongoDB:
    useNewUrlParser: true,
    useUnifiedTopology: true
    
};
const connectWithRetry = () => {
    console.log('MongoDB connection with retry')
    mongoose.connect("mongodb+srv://rest-api:idlfZ9V7rdk1KF5v@cluster0-djvd1.gcp.mongodb.net/rest-api?retryWrites=true&w=majority", options).then(()=>{
        console.log('MongoDB is connected');
        var db = mongoose.connection;
        db.db.listCollections({name: 'users'}).next(function(err1, res1) {
            if (!res1) {
                let salt = crypto.randomBytes(16).toString('base64');
                let hash = crypto.createHmac('sha512', salt).update("admin123").digest("base64");
                let temp_password = salt + "$" + hash;
                var adminObject = { firstName: "Admin", lastName: "POS", username: "admin", password: temp_password, permissionLevel: 10, _v: 0};
                db.collection("users").insertOne(adminObject, function(err, res) {
                    if (err) throw err;
                    console.log("Admin account has been added...");
                    db.close();
                });
            }   
        });
    }).catch(err=>{
        console.log('MongoDB connection unsuccessful, retry after 5 seconds. ', ++count);
        setTimeout(connectWithRetry, 5000)
    });
};

connectWithRetry();

exports.mongoose = mongoose;