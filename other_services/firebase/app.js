var placeSearch, autocomplete;
  var componentForm = {
    street_number: 'short_name',
    route: 'long_name',
    locality: 'long_name',
    administrative_area_level_1: 'short_name',
    country: 'long_name',
    postal_code: 'short_name'
  };

function initAutocomplete() {
  // Create the autocomplete object, restricting the search to geographical
  // location types.
  autocomplete = new google.maps.places.Autocomplete(
    /** @type {!HTMLInputElement} */(document.getElementById('autocomplete')),
    {types: ['geocode']});

  // When the user selects an address from the dropdown, populate the address
  // fields in the form.
  autocomplete.addListener('place_changed', fillInAddress);
}

function fillInAddress() {
  // Get the place details from the autocomplete object.
  var place = autocomplete.getPlace();

  for (var component in componentForm) {
    document.getElementById(component).value = '';
    document.getElementById(component).disabled = false;
  }

  // Get each component of the address from the place details
  // and fill the corresponding field on the form.
  for (var i = 0; i < place.address_components.length; i++) {
    var addressType = place.address_components[i].types[0];
    if (componentForm[addressType]) {
      var val = place.address_components[i][componentForm[addressType]];
      document.getElementById(addressType).value = val;
    }
  }
}

// Bias the autocomplete object to the user's geographical location,
// as supplied by the browser's 'navigator.geolocation' object.
function geolocate() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
      var geolocation = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };
      var circle = new google.maps.Circle({
        center: geolocation,
        radius: position.coords.accuracy
      });
      autocomplete.setBounds(circle.getBounds());
    });
  }
}

var firebaseConfig = {
  apiKey: "AIzaSyDnPdsIWV4WAS5c7FhncN37VCvin7NG_l0",
  authDomain: "hawkeye-2020.firebaseapp.com",
  databaseURL: "https://hawkeye-2020.firebaseio.com",
  projectId: "hawkeye-2020",
  storageBucket: "hawkeye-2020.appspot.com",
  messagingSenderId: "48441001259",
  appId: "1:48441001259:web:542f11e00a8451bdcbf16e",
  measurementId: "G-Q5BYB2QPHC"
  };
  firebase.initializeApp(firebaseConfig);
  
  // Reference messages collection
  var messagesRef = firebase.database().ref('passport verification');
  // Listen for form submit
  document.getElementById('form_passport').addEventListener('submit', submitForm);
  // Submit form
  function submitForm(e){
  e.preventDefault();
  // Get values
  var passport = getInputVal('passport');
  var aadhaar = getInputVal('aadhaar');
  var comment = getInputVal('comment');
  var police =  getInputVal('yes');
  
  
  saveMessage(passport,aadhaar,comment,police);
  document.querySelector('.alert').style.display = 'block';
  setTimeout(function(){
  document.querySelector('.alert').style.display = 'none';
  },3000);
  document.getElementById('form_passport').reset();
  }
  function getInputVal(id){
  return document.getElementById(id).value;
  }
  
  function saveMessage(passport,aadhaar,comment,police){
  var newMessageRef = messagesRef.push();
  newMessageRef.set({
  passport:passport,
  aadhaar:aadhaar,
  comment:comment,
  police_case:police
  });

  }

  