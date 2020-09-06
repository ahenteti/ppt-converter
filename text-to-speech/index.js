const gTTS = require('gtts'); 
const fs = require('fs');
const path = require('path');

if (process.argv.length <= 2) {
    console.log("Usage: " + __filename + " path/to/directory");
    process.exit(-1);
}
 
const dir = process.argv[2];
 
fs.readdir(dir, function(error, files) {
    if(error) { throw new Error(error); } 
    files.filter(file => /\.txt$/.test(file)).forEach(toSpeech);
});

function toSpeech(file) {
    const fileContent = fs.readFileSync(file, "utf8")
    const speechFileName = path.basename(file, '.txt') + '.mp3';
    const gtts = new gTTS(fileContent, 'en'); 
    gtts.save(speechFileName, function (error, result){ 
        if(error) { throw new Error(error); } 
    });
}