const ProductModel = require('../models/products.model');

exports.insert = (req, res) => {
    ProductModel.addProduct(req.body)
        .then((result) => {
            res.status(201).send({id: result._id});
        }).catch(err=>{console.log("The product cannot be added.");});;
};

exports.list = (req, res) => {
    let limit = req.query.limit && req.query.limit <= 100 ? parseInt(req.query.limit) : 10;
    let page = 0;
    if (req.query) {
        if (req.query.page) {
            req.query.page = parseInt(req.query.page);
            page = Number.isInteger(req.query.page) ? req.query.page : 0;
        }
    }
    ProductModel.list(limit, page)
        .then((result) => {
            res.status(200).send(result);
        }).catch(err=>{console.log("The list is empty");});
};

exports.getById = (req, res) => {
    ProductModel.findById(req.params.productId)
        .then((result) => {
            res.status(200).send(result);
        }).catch(err=>{console.log("The id doesn't exists");});
};

exports.getByName = (req, res) => {
    ProductModel.findByName(req.params.item_name_lang, req.params.productName)
        .then((result) => {
            res.status(200).send(result);
        }).catch(err=>{console.log("The item doesn't exists");});
};

exports.getByBarcode = (req, res) => {
    ProductModel.findByBarcode(req.params.barcode)
        .then((result) => {
            res.status(200).send(result);
        }).catch(err=>{console.log("The barcode doesn't exists");});
};

exports.getByCategory = (req, res) => {
    ProductModel.findByCategory(req.params.category)
        .then((result) => {
            res.status(200).send(result);
        }).catch(err=>{console.log("The category doesn't exists");});
};

exports.patchById = (req, res) => {
    ProductModel.editProduct(req.params.productId, req.body)
        .then((result) => {
            res.status(204).send({});
        }).catch(err=>{console.log("The id doesn't exists");});;

};

exports.removeById = (req, res) => {
    ProductModel.removeById(req.params.productId)
        .then((result)=>{
            res.status(204).send({});
        }).catch(err=>{console.log("The id doesn't exists");});;
};