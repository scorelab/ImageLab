
var blocklyDiv = document.getElementById("blocklyDiv");

var toolbox = document.getElementById("toolbox");

var options = {
	toolbox: toolbox,
	trashcan: true,
	media: 'https://blockly-demo.appspot.com/static/media/',
	scrollbars: true,
	sounds: true,
	oneBasedIndex: true,
	zoom: {
		controls: true,
		wheel: false,
		startScale: 1,
		maxScale: 3,
		minScale: 0.3,
		scaleSpeed: 1.1
	}
};
