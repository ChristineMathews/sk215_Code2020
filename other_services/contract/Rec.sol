pragma solidity >=0.4.21 <0.7.0;
contract Rec{
    uint256 public id=0;
    struct data{
        uint256 idnum;
        string ocr;
        string offence;
    }
   
    constructor() public {
    
    
    }
    event dataAdded(uint256 id,string ocr, string offence);
   mapping(uint256 => data)public Data;
   function storeData(string memory _ocr, string memory _offence) public{
       id++;
       Data[id] = data(id,_ocr,_offence);
       emit dataAdded(id,_ocr,_offence);

   }
    // function findOffence(string memory  _ocr) public view returns(string memory){
    //     for(uint i=1; i<=id ;i++){
    //         if(Data[i].ocr == _ocr){
    //             return Data[i].offence;
    //         }
    //     }
    // }
}