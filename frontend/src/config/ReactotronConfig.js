if (process.env.NODE_ENV === 'development') {
    const Reactotron = require('reactotron-react-js').default;
    const { reactotronRedux } = require('reactotron-redux');

    Reactotron
        .configure({name: 'React Native Demo'})
        .use(reactotronRedux())
        .connect();
}