const gTTS = require('gtts'); 
const fs = require('fs');
const path = require('path');
var say = require("say");

if (process.argv.length <= 2) {
    console.log("Usage: " + __filename + " path/to/directory");
    process.exit(-1);
}
 
const dir = process.argv[2];
 
fs.readdir(dir, function(err, files) {
    if(err) { throw new Error(err); } 
    files.filter(file => /\.txt$/.test(file)).forEach(toSpeech);
});

function toSpeech(file) {
    const fileContent = fs.readFileSync(file, "utf8")
    const speechFilename = path.basename(file, '.txt') + '.wav';
    say.export(fileContent, 'Microsoft David Desktop', 1, speechFilename, function(err) {
        if(err) { throw new Error(err); }
    });
}