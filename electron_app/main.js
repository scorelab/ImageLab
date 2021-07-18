'use strict';

console.log("main process working");

const electron = require("electron");
const { app, BrowserWindow } = electron;

app.on("ready", () => {
    let mainWindow = new BrowserWindow({
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
        }, height: 700, width: 1500, show: false
    });
    mainWindow.loadURL(`file://${__dirname}/index.html`);
    mainWindow.once("ready-to-show", () => {
        mainWindow.show();
    });

    mainWindow.on('closed', function () {

        mainWindow = null;
    })
});
