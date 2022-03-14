const functions = require("firebase-functions");

const admin = require('firebase-admin');
admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });

exports.pushNotification = functions.database.ref('/Aquarium/{Aquarium1}').onWrite((change, context) => {
    console.log('Push notification event triggered');

    const valueObject = change.after.val();
    var text = "";
    var notif = true;
    var problem = "pas de proubleme"
    title = valueObject.nom  + " - " + valueObject.salle
    if (valueObject.temperature > 20){
       // var temp =  valueObject.temperature - valueObject.espece.tempMax
        text = "Température trop chaude";
        problem = "Chaud"

    } else if (valueObject.temperature < 15){
        text = "Température trop froide";
        problem = "Froid"
    }
    else {
        var notif = false;
    }
    if (notif == true){
    const payload = {
        notification: {
            title: title,
            body: text,
            sound: "default"
        },
        data : {
            "id" : valueObject.id,
            "problem" : problem,
            "temperature" : valueObject.temperature.toString()
        }
    };

    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };


    return admin.messaging().sendToTopic("all", payload, options);
    } else {
        return false;
    }
});
