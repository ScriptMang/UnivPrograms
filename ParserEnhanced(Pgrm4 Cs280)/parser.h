/*
 * parser.h
 *
 *  Created on: Oct 23, 2017
 *      Author: gerardryan
 */

#ifndef PARSER_H_
#define PARSER_H_

#include <iostream>
#include <string>
#include "lexer.h"
using std::istream;
using std::string;
using std::stoi;

extern void error(int linenum, const string& message);

enum TypeForNode { INT_TYPE, STRING_TYPE, ERROR_TYPE };

class ParseTree {
  int      linenumber;
  ParseTree  *left;
  ParseTree  *right;
public:
  TypeForNode tn = ERROR_TYPE;

  ParseTree(int n, ParseTree *l = 0, ParseTree *r = 0) : linenumber(n), left(l), right(r) {}
  virtual ~ParseTree() {}

  ParseTree* getLeft() const {return left;}
  ParseTree* getRight() const {return right;}
  int getLineNumber() const { return linenumber; }

  TypeForNode GetType() const { return tn; }
  virtual int GetIntValue() const { throw "no integer value"; }
  virtual string GetStringValue() const { throw "no string value"; }
  virtual int isInt(){ return 0; }
};

extern ParseTree *  Prog(istream* in);
extern ParseTree *  StmtList(istream* in);
extern ParseTree *  Stmt(istream* in);
extern ParseTree *  Decl(istream* in);
extern ParseTree *  Set(istream* in);
extern ParseTree *  Print(istream* in);
extern ParseTree *  Expr(istream* in);
extern ParseTree *  Term(istream* in);
extern ParseTree *  Primary(istream* in);
extern void evaluate(TokenType tk, string result);
extern void evaluate(TokenType tk, int result);
extern int idCount;
extern int setCount;
extern int plCount;
extern int multCount;

#endif /* PARSER_H_ */


