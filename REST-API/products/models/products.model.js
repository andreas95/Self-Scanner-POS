const mongoose = require('../../common/services/mongoose.service').mongoose;
const Schema = mongoose.Schema;

const productSchema = new Schema({
    item_name_EN: { type : String, required : true},
    item_name_IT : String,
    item_name_RO : String,
    price : {type : String, required : true},
    item_photo : String,
    units : Number,
    barcode : { type : String, required : true , unique : true, dropDups: true },
    category : { type : String, required : true}
});

productSchema.virtual('id').get(function () {
    return this._id.toHexString();
});

// Ensure virtual fields are serialised.
productSchema.set('toJSON', {
    virtuals: true
});

productSchema.findById = function (cb) {
    return this.model('Products').find({id: this.id}, cb);
};

productSchema.findByBarcode = function (cb) {
    return this.model('Products').find({barcode: this.barcode}, cb);
};

productSchema.findByCategory = function (cb) {
    return this.model('Products').find({category: this.category}, cb);
};

const Product = mongoose.model('Products', productSchema);


exports.findByName = (item_name_lang, name) => {
    return new Promise((resolve, reject) => {
        temp_json_obj = JSON.parse('{ "' + item_name_lang + '": { "$regex": "' + name + '", "$options": "i" }}');
        Product.find(temp_json_obj)
            .exec(function (err, products) {
                if (err) {
                    reject(err);
                } else {
                    resolve(products);
                }
            })
    });
};

exports.findByBarcode = (barcode) => {
    return Product.findByBarcode(barcode)
        .then((result) => {
            result = result.toJSON();
            delete result._id;
            delete result.__v;
            return result;
        });
};

exports.findByCategory = (category) => {
    return Product.findByCategory(category)
        .then((result) => {
            result = result.toJSON();
            delete result._id;
            delete result.__v;
            return result;
        });
};

exports.findById = (id) => {
    return Product.findById(id)
        .then((result) => {
            result = result.toJSON();
            delete result._id;
            delete result.__v;
            return result;
        });
};

exports.addProduct = (productData) => {
    const product = new Product(productData);
    return product.save();
};

exports.list = (perPage, page) => {
    return new Promise((resolve, reject) => {
        Product.find()
            .limit(perPage)
            .skip(perPage * page)
            .exec(function (err, products) {
                if (err) {
                    reject(err);
                } else {
                    resolve(products);
                }
            })
    });
};

exports.editProduct = (id, productData) => {
    return Product.findOneAndUpdate({
        _id: id
    }, productData);
};

exports.removeById = (productId) => {
    return new Promise((resolve, reject) => {
        Product.deleteMany({_id: productId}, (err) => {
            if (err) {
                reject(err);
            } else {
                resolve(err);
            }
        });
    });
};

