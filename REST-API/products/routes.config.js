const ProductsController = require('./controllers/products.controller');
const PermissionMiddleware = require('../common/middlewares/auth.permission.middleware');
const ValidationMiddleware = require('../common/middlewares/auth.validation.middleware');
const config = require('../common/config/env.config');

const ADMIN = config.permissionLevels.ADMIN;
const MANAGER = config.permissionLevels.MANAGER;

exports.routesConfig = function (app) {
    app.post('/products', [
        ValidationMiddleware.validJWTNeeded,
        PermissionMiddleware.minimumPermissionLevelRequired(MANAGER),
        ProductsController.insert
    ]);
    app.get('/products', [
        ValidationMiddleware.validJWTNeeded,
        PermissionMiddleware.minimumPermissionLevelRequired(MANAGER),
        ProductsController.list
    ]);
    app.get('/products/:productId', [
        ValidationMiddleware.validJWTNeeded,
        PermissionMiddleware.minimumPermissionLevelRequired(MANAGER),
        ProductsController.getById
    ]);
    app.patch('/products/:productId', [
        ValidationMiddleware.validJWTNeeded,
        PermissionMiddleware.minimumPermissionLevelRequired(MANAGER),
        ProductsController.patchById
    ]);
    app.delete('/products/:productId', [
        ValidationMiddleware.validJWTNeeded,
        PermissionMiddleware.minimumPermissionLevelRequired(ADMIN),
        ProductsController.removeById
    ]);
};
