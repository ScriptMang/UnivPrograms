

#ifndef nonterminals_h
#define nonterminals_h

#include "parser.h"
#include "terminals.h"
#include <string>
#include<algorithm>
#include<memory>
using namespace std;
int numbTypeErr;

class StatementList : public ParseTree {
public:
  StatementList(ParseTree *first, ParseTree *rest) : ParseTree(0, first, rest){}

};

class Addition : public ParseTree {
  int value;
  string stringValue;
public:
  Addition(int line, ParseTree *op1, ParseTree *op2) : ParseTree(line, op1, op2) {
    if (op1->GetType() == INT_TYPE && op1->GetType() == op2->GetType()) {
      value = op1->GetIntValue() + op2->GetIntValue();
      tn = INT_TYPE;
      return;
    } else if (op1->GetType() == STRING_TYPE && op1->GetType() == op2->GetType()) {
      stringValue = op1->GetStringValue() + op2->GetStringValue();
      tn = STRING_TYPE;
      return;
    } else if (op1->GetType() == INT_TYPE && op2->GetType() == STRING_TYPE) {
      error(op1->getLineNumber(), "cannot add  a int with a string");
      return;
    } else if (op1->GetType() != op2->GetType()) {
      if (numbTypeErr == 0)
        error(op2->getLineNumber(), "type error");
      ++numbTypeErr;
      return;
    }
  }

  int GetIntValue() const {
    if (tn == STRING_TYPE) {throw "no integer value";};
    return value;
  }

  string GetStringValue() const {
    if (tn == INT_TYPE) {throw "no string value";};
    return stringValue;
  }
};

class Subtraction : public ParseTree {
  int value;
public:
  Subtraction(int line, ParseTree *op1, ParseTree *op2) : ParseTree(line, op1, op2) {
    if (op1->GetType() != INT_TYPE || op2->GetType() != INT_TYPE) {
      error(op1->getLineNumber(), "type error");
      return;
    }
    tn = INT_TYPE;
    value = op1->GetIntValue() - op2->GetIntValue();
  }

  int GetIntValue() const {
    if (tn == STRING_TYPE) {cerr << "no integer value"<< endl;};
    return value;
  }
};

class Multiplication : public ParseTree {
  int value;
  string stringValue;
public:
  Multiplication(int line, ParseTree *op1, ParseTree *op2) : ParseTree(line, op1, op2) {
    if ((op1->GetType() == STRING_TYPE && op2->GetType() == STRING_TYPE) ||
        op1->GetType() == ERROR_TYPE || op2->GetType() == ERROR_TYPE) {
      error(op1->getLineNumber(),"type error" );
      return;
    }

    if (op1->GetType() == INT_TYPE && op2->GetType() == STRING_TYPE) {
      tn = STRING_TYPE;
      for (int i = 0; i < op1->GetIntValue(); i++) {
        stringValue += op2->GetStringValue();
      }
    } else if (op1->GetType() == STRING_TYPE && op2->GetType() == INT_TYPE) {
      tn = STRING_TYPE;
      for (int i = 0; i < op2->GetIntValue(); i++) {
        stringValue += op1->GetStringValue();
      }
    } else {
      tn = INT_TYPE;
      value = op1->GetIntValue() * op2->GetIntValue();
    }
  }

  int GetIntValue() const {
    if (tn == STRING_TYPE) {cerr << "no integer value" << endl;};
    return value;
  }

  string GetStringValue() const {
    if (tn == INT_TYPE) {cerr <<"no string value" << endl;};
    return stringValue;
  }
};

class Division : public ParseTree {
  int value;
  string stringValue;
public:
  Division(int line, ParseTree *op1, ParseTree *op2) : ParseTree(line, op1, op2) {
    if (op1->GetType() == STRING_TYPE && op2->GetType() == STRING_TYPE) {
      tn = STRING_TYPE;
      string num = op1->GetStringValue();
      // Loops until all of the Double Quotes aren't present
      // within the string
      while (num.find('"') != string::npos) {
        num.erase(std::find(num.begin(), num.end(), '"')); // Erase
      }
      string denom = op2->GetStringValue();
      while (denom.find('"') != string::npos) {
        denom.erase(std::find(denom.begin(), denom.end(), '"')); // Erase
      }
      int pos = int(num.find(denom));
      if (pos == -1) {
        stringValue = num;
        return;
      }
      num.erase(pos, denom.length());

      stringValue = num;
      return;
    } else if (op1->GetType() == INT_TYPE && op2->GetType() == INT_TYPE) {
      tn = INT_TYPE;
      value = op1->GetIntValue() / op2->GetIntValue();
      return;
    }
    error(op1->getLineNumber(), "type error");
    return;
  }

  int GetIntValue() const {
    if (tn == STRING_TYPE) {cerr << "no integer value"<<endl;};
    return value;
  }

  string GetStringValue() const {
    if (tn == INT_TYPE) {cerr << "no string value"<<endl;};
    return stringValue;
  }
};

class Declaration: public ParseTree {
public:
  Declaration(int line, Identifier *identifier) : ParseTree(line, NULL, NULL)
  { tn = identifier->GetType();}
};

class SetIdentifier: public ParseTree {
  Identifier *identifier;
public:
  SetIdentifier(int line, Identifier *identifier, ParseTree *expr): ParseTree(line, expr, NULL)  {
    if (identifier->GetType() == ERROR_TYPE || expr->GetType() == ERROR_TYPE) {
      error(identifier->getLineNumber(),"SET: identifier or expr are of ERROR_TYPE" );
      return;
    }

    if (identifier->GetType() != expr->GetType()) {
      error(line,"type error" );
      return;
    }
    identifier = identifier;
    tn = expr->GetType();

    if (tn == INT_TYPE) {
      identifier->intValue = expr->GetIntValue();
      return;
    }

    if (tn == STRING_TYPE) { identifier->stringValue = expr->GetStringValue(); }
  }
};

class print: public ParseTree {
public:
  print(int line, TokenType &tt, ParseTree *expr) : ParseTree(line, expr, 0) {
    if (expr->GetType() == ERROR_TYPE) {
      return;
    }
    if (tt == T_PRINT) {
      if (expr->GetType() == INT_TYPE)
        evaluate(tt, expr->GetIntValue());
      else if (expr->GetType() == STRING_TYPE) {
        string str = expr->GetStringValue();
        while (str.find('"') != string::npos) {
          str.erase(std::find(str.begin(), str.end(), '"')); // Erase
        }
        evaluate(tt,  str);
      }
    }  else if (tt == T_PRINTLN) {
          if (expr->GetType() == INT_TYPE)
            evaluate(tt, expr->GetIntValue());
          else if (expr->GetType() == STRING_TYPE) {
            string str = expr->GetStringValue();
            while (str.find('"') != string::npos) {
              str.erase(std::find(str.begin(), str.end(), '"')); // Erase
            }
            evaluate(tt, str);
          }
      }
  }
};
#endif /* nonterminals_h */


