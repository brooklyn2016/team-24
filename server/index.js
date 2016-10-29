var express = require('express');
var firebase = require('firebase');
var request = require('request');
var bodyParser = require('body-parser');
var https = require('https');
var fs = require('fs');

var metas = {}

// Initialize Firebase
firebase.initializeApp({
  serviceAccount: './lib/BRIC-74413bf89aef.json',
  databaseURL: 'bric-dd7cd.firebaseio.com'
});

var db = firebase.database();

var sendConfirmationEmail = function(submission) {

  var email = submission.email;
  // do nothing if user has no email or if email confirmation
  // was already sent
  if (submission.emailConfirmationSent || ! email) {
    return;
  }

  var zapierHook = 'https://hooks.zapier.com/hooks/catch/1761861/679qd6/';
  request({
    url: zapierHook,
    method: 'POST',
    json: submission
  })
}

var onNewComplete = function(snapshot) {
  var val = snapshot.val();
  var key = snapshot.key;
  if (val.status !== 'scheduled') {
    return;
  }
  sendConfirmationEmail(val);
  if (val.email && val.emailConfirmationSent) {
    db.ref('submissions/' + key + '/emailConfirmationSent').set(true);
  }
}

// Listen to database updates
var submissionsRef = db.ref('submissions');
submissionsRef.orderByChild('emailConfirmationSent').equalTo(null).on('child_changed', onNewComplete);

var app = express();
app.use(bodyParser.json());

var port = process.env.PORT || 8000;

app.listen(port);

app.get('/', function(req, res) {
  res.send(200, 'Hello!')
})

// mock a request to the zapier webhook
// @param {string} req.body.email - the user's email
app.post('/send_email_confirmation', function(req, res) {
  var body = req.body;
  console.log(req)
  if (!body || !body.email) {
    res.sendStatus(400);
  }
  sendConfirmationEmail(body);
  res.sendStatus(200);
})

// messenger webhook
app.get('/messenger_webhook', function(req, res) {
  var VALIDATION_TOKEN = '09384759347934';
  if (req.query['hub.mode'] === 'subscribe' &&
      req.query['hub.verify_token'] === VALIDATION_TOKEN) {
    console.log("Validating webhook");
    res.status(200).send(req.query['hub.challenge']);
  } else {
    console.error("Failed validation. Make sure the validation tokens match.");
    res.sendStatus(403);
  }
});

var processError = function(senderID, recipientid, prevMessage, metadata) {
    metadata.nextAction = undefined;
    metadata.isSubmitting = false;
    sendTextMessage(senderID, recipientid, 'Hi! Send me a video for a chance to be on BRIC TV.', metadata);
}

var processIsEvent = function(senderID, recipientid, prevMessage, metadata) {
  if (!prevMessage) {
    sendTextMessage(senderID, recipientid, 'Can you repeat that? You can only tell me yes and no right now.', metadata);
  }
  var mess = prevMessage.toUpperCase();
  if (mess === 'YES') {
    metadata.nextAction = 'eventCategory';
    sendTextMessage(senderID, recipientid, 'What event does your video depict: 1) Father’s Day\n 2) Afropunk\n 3) House Party\n or 4) None of the above ', metadata);
    return;
  } else if (mess === 'NO') {
    metadata.nextAction = 'subject';
    sendTextMessage(senderID, recipientid, 'What word best describes your video: 1) family-friendly\n 2) comedy\n 3) None of the above', metadata);
    return;
  } else {
    sendTextMessage(senderID, recipientid, 'Can you repeat that? You can only tell me yes and no right now.', metadata);
    return;
  }
}

var processEventCategory = function(senderID, recipientid, prevMessage, metadata) {
  var num = parseInt(prevMessage);
  if (isNaN(num) || !num || num > events.length + 1) {
    sendTextMessage(senderID, recipientid, 'You entered an invalid value. Try again', metadata);
  }
  var events = ['Father’s Day', 'Afropunk', 'House Party']
  metadata.tag = (num === events.length + 1) ? 'Other' : events[num];
  metadata.nextAction = 'addr1';
  sendTextMessage(senderID, recipientid, 'What is your address?', metadata);
}

var processSubject = function(senderID, recipientid, prevMessage, metadata) {
  var num = parseInt(prevMessage);
  if (isNaN(num) || !num || num > events.length + 1) {
    sendTextMessage(senderID, recipientid, 'You entered an invalid value. Try again', metadata);
  }
  var categories = ['family-friendly', 'comedy'];
  metadata.tag = (num === categories.length + 1) ? 'Other' : categories[num];
  metadata.nextAction = 'addr1';
  sendTextMessage(senderID, recipientid, 'What is your address?', metadata);
}

var processAddr1 = function(senderID, recipientid, prevMessage, metadata) {
  metadata.addr1 = prevMessage;
  metadata.nextAction = 'addr2';
  sendTextMessage(senderID, recipientid, 'What is the second part of your address (if applicable, or None if none)?', metadata);
}

var processAddr2 = function(senderID, recipientid, prevMessage, metadata) {
  metadata.addr2 = (prevMessage.toUpperCase === 'NONE') ? '' : prevMessage;
  metadata.nextAction = 'What is your phone number?';
}

var processPhone = function(senderID, recipientid, prevMessage, metadata) {
  metadata.phone = prevMessage;
  metadata.isSubmitting = false;
  metadata.nextAction = undefined;
  sendTextMessage(senderID, recipientid, prevMessage, metadata);
}

var sendTextMessage = function(senderID, recipientid, message, metadata, quickReplies) {
  metas[recipientid] = metadata;
  var stringedMeta = JSON.stringify(metadata);
  var messageData = {
    recipient: {
      id: senderID
    },
    message: {
      text: message,
      metadata: stringedMeta,
    }
  };
  if (quickReplies) {
    messageData.quick_replies = quickReplies;
  }
  request({url: 'https://graph.facebook.com/v2.6/me/messages?access_token=EAAI4Xci81OMBAIsdjgPov4fvq1tBk1uM8x2Puz9Vnnh7ZCfz0Ej1hVP0lTN2Gnzz59MBIRv6t8IDdVZB1WZBedVwJRyWg0hXMZCO1ZAxRjU7VNw71vMKnhxOP6Lvm95DND92NufegPYykCDYZAt58qvYIDuI24eEqJ4LuVLGQMTAZDZD',
    method: 'POST',
    json: messageData
  });
}

var onReceivedMessage = function(e) {
  console.log(e);
  var senderID = e.sender.id;
  var recipientid = e.recipient.id;
  var message = e.message;
  var meta = (message.metadata) ? JSON.parse(message.metadata) : metas[e.recipient.id] || {};
  console.log(meta);
  if (message.attachments) {
    if (message.attachments.length !== 1 || message.attachments[0].type !== 'video') {
      meta.isSubmitting = false;
      sendTextMessage(senderID, e.recipient.id, 'Invalid file type', meta);
    } else {
      meta.isSubmitting = true;
      meta.nextAction = 'isEvent'
      sendTextMessage(senderID, e.recipient.id, 'Your submission is being processed. In the meantime can you tell me more about your app? \n' +
        'Is your video about a current event?\n', meta);
      return;
    }
  } else if (message.text) {
    if (!meta.isSubmitting) {
      processError(senderID, recipientid, message, meta);
      return;
    }
    var processFunction;
    switch (meta.nextAction) {
      case 'isEvent':
        processFunction = processIsEvent;
        break;
      case 'eventCategory':
        processFunction = processEventCategory;
        break;
      case 'subject':
        processFunction = processSubject;
        break;
      case 'addr1':
        processFunction = processAddr1;
        break;
      case 'addr2':
        processFunction = processAddr2;
        break;
      case 'phone':
        processFunction = processPhone;
      default:
        processFunction = processError;
        break;
    }
    processFunction(senderID, e.recipient.id, message.text, meta);
  }
}

app.post('/messenger_webhook', function(req, res){
  var data = req.body;
  if (data.object === 'page') {
    // Iterate over each entry
    // There may be multiple if batched
    data.entry.forEach(function(pageEntry) {
      var pageID = pageEntry.id;
      var timeOfEvent = pageEntry.time;

      // Iterate over each messaging event
      pageEntry.messaging.forEach(function(messagingEvent) {
        // don't do anything if you don't receive a message
        if (messagingEvent.message) {
          onReceivedMessage(messagingEvent);
        }
      });
    });

    // Assume all went well.
    //
    // You must send back a 200, within 20 seconds, to let us know you've
    // successfully received the callback. Otherwise, the request will time out.
    res.sendStatus(200);
  }
})
