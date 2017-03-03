import React from 'react';
import reducer from 'reducers';
import { createStore, applyMiddleware, compose } from 'redux';
import DevTools from 'config/devtools';
import promiseMiddleware from 'config/promiseMiddleware';
import Reactotron from 'reactotron-react-js'

let middlewares = [applyMiddleware(promiseMiddleware)];
let buildStore = createStore;

if (process.env.NODE_ENV === 'development') {
    middlewares = [applyMiddleware(promiseMiddleware), DevTools.instrument()];
    buildStore = Reactotron.createStore;
}

var initialize = (initialState = {}) => {

  const store = buildStore(reducer, initialState, compose(...middlewares));

  if (module.hot) {
    // Enable Webpack hot module replacement for reducers
    module.hot.accept('../reducers', () => {
      const nextReducer = require('../reducers');
      store.replaceReducer(nextReducer);
    });
  }
  return store;
};

export default initialize;

