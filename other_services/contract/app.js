App = {
  web3Provider: null,
  contracts: {},
  account: '0x0',


  init: function() {
    return App.initWeb3();
  },

  initWeb3: async function() {
    if (typeof web3 !== 'undefined') {
      // If a web3 instance is already provided by Meta Mask.
      App.web3Provider = web3.currentProvider;
      web3 = new Web3(web3.currentProvider);
    } else {
      // Specify default instance if no web3 instance provided
      App.web3Provider = new Web3.providers.HttpProvider('http://localhost:7545');
      web3 = new Web3(App.web3Provider);
    }
    return App.initContract();
  },

  initContract: function() {
    $.getJSON("Rec.json", function(rec) {
      // Instantiate a new truffle contract from the artifact
      App.contracts.Rec = TruffleContract(rec);
      // Connect provider to interact with contract
      App.contracts.Rec.setProvider(App.web3Provider);

      return App.userProfile();
    });
  },

 userProfile: function(){
    //document.getElementById("pageTitle").innerHTML = "Userview"
    web3.eth.getCoinbase(function(err, account) {
      if (err === null) {
        App.account = account;
        // alert(App.account);
        $('#add').submit(function(event){
          event.preventDefault()
          App.contracts.Rec.deployed().then(async function(instance){
           //var geriatricInstance = instance;
           var ocr = $('input#ocr').val();
           var offense = $('input#offense').val();
          //  alert(ocr);
           return instance.storeData(ocr,offense,{from:App.account});
          
          });
      });
      }  
    });
  
  }
 
};

$(function() {
  $(window).load(function() {
    App.init();
  });
});
