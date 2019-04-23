// Andy Peralta
// Cs280 sect005
//
/////////////////////

#include <iostream>
#include<fstream>
#include "parser.h"
#include <vector>
#include <string>
#include "lexer.h"
using namespace std;

int numbError;
vector<string> err;
vector<string> eval; //Answer to good prints
string file;
int lineNumber = 1;
using std::cin;


void error(int linenum, const string& message) {
  string mssg (file + ":" +  to_string(linenum) + ":" + message + "\n");
  err.push_back(mssg);
  ++numbError;
}

void evaluate(TokenType tk, string result) {
  if (tk == T_PRINTLN) {
    string mssg(result+"\n");
    eval.push_back(mssg);
  } else {
    string mssg(result);
    eval.push_back(mssg);
  }
}

void evaluate(TokenType tk, int result) {
  string mssg;
  if (tk == T_PRINTLN) {
    string mssg(to_string(result) + "\n");
    eval.push_back(mssg);
  } else {
    string mssg(to_string(result));
    eval.push_back(mssg);
  }
}

int main(int argc, char *argv[])
{

  ifstream ifile;
  istream *in = &cin;

  for (int i = 1; i < argc; i++) {
    //Checks for invalidflags
   if (in != &cin) {
      cerr << "TOO MANY FILES" << endl;
      return -1;
    } else {
      file = argv[i];
      ifile.open(argv[i]);
      // Checks for badFiles
      if (!ifile.is_open()) {
        cerr << argv[i] << " FILE NOT FOUND\n";
        return -1;
      }
      in = &ifile;
    }
  }

  // while loop Prints out All the TokenTypes
  // Breaks on the Tokens: T_DONE and T_ERROR

  // Integer Variables that go with Done Token

  if (argc > 1) {
    ParseTree* tree = Prog(in);

    if (numbError > 0) {
      for (string s: err)
        cout << s;
      return 0;
    }
    else {
      for (string res : eval)
        cout << res;
    }
    if( tree == 0 )  {
      // there was some kind of parse error
      return 1;
    }
  }
  else {
    // Recursively looks through all input
    ParseTree *tree = Prog( in );
    if (numbError > 0) {
      for (string s: err)
        cout << s;
      return 0;
    }
    else {
      for (string res : eval)
        cout << res;
    }

    if( tree == 0 ) {
      // there was some kind of parse error
      return 1;
    }

  }
  ifile.close();
  return 0;
}


