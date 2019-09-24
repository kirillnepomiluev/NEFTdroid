// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();
const db = admin.database();

exports.sendFollowerNotification = functions.database.ref('/notifications/{followedUid}/{notifId}')
    .onCreate(async (change, context) => {
      const followedUid = context.params.followedUid;
      // If un-follow we exit the function.

      console.log('We have a new followed UID:', followedUid);
	  console.log('change:', change);
	  console.log('context:', context);
	  console.log('change data:', change._data);
	  console.log('change data name:', change._data.name);
	  console.log('change data id:', change._data.id);
	  const newNot = change._data;
	  const name = newNot.name;
	  console.log('name:', name);

	
  
	 
	  
      // Notification details.
      const payload = {
        notification: {
          title: 'У вас новое событие',
		  body: change._data.name
        },
		topic: followedUid
      };

      // Send notifications to all tokens.
      const response = await admin.messaging().send(payload);
      // For each message check if there was an error.
      const tokensToRemove = [];

      return Promise.all(tokensToRemove);
    }); 
// firebase deploy --only functions

/*
exports.sendFollowerNotification = functions.database.ref('/notifications/{followedUid}')
    .onWrite(async (change, context) => {
      const followedUid = context.params.followedUid;
      // If un-follow we exit the function.
      if (!change.after.val()) {
        return console.log('User ', followedUid);
      }
      console.log('We have a new followed UID:', followedUid);
	  console.log('change:', change);
	  console.log('context:', context);


	const cngsAfter = change.after.val();
	console.log('changes after:', cngsAfter);
	const cngsBefore = change.before.val();
	console.log('changes before:', cngsBefore);
	
	const dbref = db.ref('/notifications/{followedUid}/{notifId}/').once('value', function (snapshot) {
		const notif = snapshot.val();		
	});
	console.log('notif:', notif);
  
	 
	  
      // Notification details.
      const payload = {
        notification: {
          title: 'У вас новое уведомление',
          body: `Нажмите, чтобы прочитать`,
        },
		topic: followedUid
      };

      // Send notifications to all tokens.
      const response = await admin.messaging().send(payload);
      // For each message check if there was an error.
      const tokensToRemove = [];

      return Promise.all(tokensToRemove);
    }); 
*/



