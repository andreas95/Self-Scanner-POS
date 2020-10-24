const config = require('./common/config/env.config.js');

const express = require('express');
const app = express();
const bodyParser = require('body-parser');

const AuthorizationRouter = require('./authorization/routes.config');
const UsersRouter = require('./users/routes.config');
const ProductsRouter = require('./products/routes.config');

app.use(function (req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Credentials', 'true');
    res.header('Access-Control-Allow-Methods', 'GET,HEAD,PUT,PATCH,POST,DELETE');
    res.header('Access-Control-Expose-Headers', 'Content-Length');
    res.header('Access-Control-Allow-Headers', 'Accept, Authorization, Content-Type, X-Requested-With, Range');
    if (req.method === 'OPTIONS') {
        return res.sendStatus(200);
    } else {
        return next();
    }
});

app.use(bodyParser.json());
AuthorizationRouter.routesConfig(app);
UsersRouter.routesConfig(app);
ProductsRouter.routesConfig(app);

app.get('*', (req, res) => {
  res.setHeader('Content-Type', 'text/html');
  res.send('<style>@import "http://fonts.googleapis.com/css?family=Roboto"; body{background:#0a9b3f;}</style><div style="color: #fff;text-align: center;font-family: Roboto;font-size: 33px;margin-top: 10%;">Self Scanner Pos - REST API</div>');
});

app.listen(config.port, function () {
    console.log('app listening at port %s', config.port);
});