var express = require('express');
var firebase = require('firebase');

// https://hooks.zapier.com/hooks/catch/1761861/679qd6/

// Initialize Firebase
firebase.initializeApp({
  serviceAccount: './lib/BRIC-74413bf89aef.json',
  databaseURL: 'bric-dd7cd.firebaseio.com'
});

var db = firebase.database();
console.log(db);
