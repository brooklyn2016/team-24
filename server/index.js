var express = require('express');
var firebase = require('firebase');
var request = require('request');
var bodyParser = require('body-parser');

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

app.listen(8000);
