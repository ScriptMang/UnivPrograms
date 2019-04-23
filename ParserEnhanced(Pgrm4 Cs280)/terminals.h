#ifndef TERMINALS_H
#define TERMINALS_H
#include "parser.h"

class IntegerConstant : public ParseTree {
  int  value;
public:
  IntegerConstant(const Token& tok) : ParseTree(tok.GetLinenum()) {
    value = stoi(tok.GetLexeme());
    tn = INT_TYPE;
  }

  TypeForNode GetType() const { return tn; }
  int GetIntValue() const override  { return value; }
  int isInt() override { return 1;}
};

class StringConstant: public ParseTree {
  string value;
public:
  StringConstant(const Token &tok) : ParseTree(tok.GetLinenum()) {
    value = tok.GetLexeme();
    tn = STRING_TYPE;
  }
  TypeForNode GetType() const { return tn; }
  string GetStringValue() const { return value; }
};

class Identifier: public ParseTree {
public:
  int intValue;
  string stringValue;
  string name;
  Identifier(const Token &tok, TypeForNode tfn, int val): ParseTree(0, NULL, NULL) {
    name = tok.GetLexeme();
    tn = tfn;
    intValue = val;
  }

  Identifier(const Token &tok, TypeForNode tfn, string stringVal): ParseTree(0, NULL, NULL) {
    name = tok.GetLexeme();
    tn = tfn;
    stringValue = stringVal;
  }

  int GetIntValue() const {
    if (tn == STRING_TYPE) throw "no integer value";
    return intValue;
  }

  string GetStringValue() const {
    if (tn == INT_TYPE) throw "no string value";
    return stringValue;
  }
};

#endif //TERMINALS_H

