module.exports = {
    open: function (arg1, arg2, success, failure) {
        cordova.exec(success, failure, "FileChooser", "open", [arg1, arg2]);
    }
};
