/*
 * parser.cpp
 *
 */

#include "parser.h"
#include "nonTerminals.h"
#include "terminals.h"
#include <map>

using std::string;
using namespace std;
int idCount = 0;
int setCount = 0;
int multCount = 0;
int plCount = 0;
int lineNumb=0;

map<string, Identifier*> identifierMap;

class ParserToken {
private:
  Token  tok;
  bool  pushedBack;

public:

  ParserToken() : pushedBack(false) {}
  Token getToken(istream *in) {
    if (pushedBack) {
      pushedBack = false;
      return tok;
    }

    return ::getToken(in);
  }
  void pushbackToken(const Token& t) {
    if (pushedBack) {
      // error
    }
    tok = t;
    pushedBack = true;
  }
} ParserToken;

ParseTree* Prog(istream* in)
{
  return StmtList(in);
}

ParseTree* StmtList(istream* in)
{
  ParseTree *stmt = Stmt(in);
  if (stmt == 0) {
    return 0;
  }
  return new StatementList(stmt, StmtList(in));
}


ParseTree* Stmt(istream* in) {
  Token t = ParserToken.getToken(in);
  ParseTree *stmtLine = nullptr;
  bool initialTokenValid = false;

  if (t.GetTokenType() == T_INT || t.GetTokenType() == T_STRING) {
    initialTokenValid = true;
    ParserToken.pushbackToken(t);
    stmtLine= Decl(in);
  } else if (t.GetTokenType() == T_SET) {
    initialTokenValid = true;
    ParserToken.pushbackToken(t);
    stmtLine = Set(in);
  } else if (t.GetTokenType() == T_PRINT || t.GetTokenType() == T_PRINTLN) {
    initialTokenValid = true;
    ParserToken.pushbackToken(t);
    stmtLine = Print(in);
  }

  if (initialTokenValid == true) {
    t = ParserToken.getToken(in);
    if (t.GetTokenType() == T_SC) {
      return stmtLine;
    } else {
      error(t.GetLinenum(), "Syntax error semicolon required");
      return 0;
    }
  }
  return 0;
}


ParseTree* Decl(istream* in) {
  // Check if Declaration previously exists, if so return error
  Token t = ParserToken.getToken(in);
  if (t.GetTokenType() == T_INT) {
    Token idTk = ParserToken.getToken(in);
    if (idTk.GetTokenType() == T_ID) {
      // Check if identifier was declared before
      if (identifierMap[idTk.GetLexeme()] != NULL) {
        error(idTk.GetLinenum(), "variable " +idTk.GetLexeme()+ " was already declared");
        return new ParseTree(idTk.GetLinenum(), NULL, NULL);
      }
      ++idCount;           //IdentifierCount
      Identifier *identifier = new Identifier(idTk, INT_TYPE, 0);
      //store value of identifier in map
      identifierMap[idTk.GetLexeme()] = identifier;

      return new Declaration(t.GetLinenum(), identifier);
    }
  } else if (t.GetTokenType() == T_STRING) {
    Token idTk = ParserToken.getToken(in);
    if (idTk.GetTokenType() == T_ID) {
      // Check if identifier was previously declared
      if (identifierMap[idTk.GetLexeme()] != NULL) {
        error(idTk.GetLinenum(), "variable " +idTk.GetLexeme()+ " was already declared");
        return new ParseTree(idTk.GetLinenum(), NULL, NULL);
      }
      ++idCount; //IdentifierCount
      Identifier *identifier = new Identifier(idTk, STRING_TYPE, 0);
      //store value of identifier in map
      identifierMap[idTk.GetLexeme()] = identifier;
      return new ParseTree(idTk.GetLinenum(), NULL, NULL);
    }
  }
  error(t.GetLinenum(), "Invalid Declaration");
  return 0;
}

ParseTree* Set(istream* in) {
  Token t = ParserToken.getToken(in);
  if (t.GetTokenType() == T_SET) {
    ++setCount;
    t = ParserToken.getToken(in);
    if (t.GetTokenType() == T_ID) {
      //get value of identifier from map and check that it was declared
      Identifier *identifier = identifierMap[t.GetLexeme()];
      if (identifier == NULL) {
        error(t.GetLinenum(), "variable " + t.GetLexeme() + " was used before being declared");
        return 0;
      }
      ParseTree * expr = Expr(in);
      if (expr == 0) {
        error(t.GetLinenum(), "Syntax error expression required");
        return 0;
      }
      return new SetIdentifier(t.GetLinenum(), identifier, expr);
    }
  }
  return 0;
}

ParseTree* Print(istream* in) {
  Token t = ParserToken.getToken(in);
  if (t.GetTokenType() == T_PRINT || t.GetTokenType() == T_PRINTLN) {
    ParseTree *exp = Expr(in);  // if the node is an expression
    if (exp == 0) {
      return 0;
    }
    TokenType tkType = t.GetTokenType();
    return new print(t.GetLinenum(), tkType, exp);
  }
  else {
    error (t.GetLinenum(), "Invalid Print");
    return 0;
  }
}

ParseTree* Expr(istream* in) {
  ParseTree *t1 = Term(in);
  Token op;

  if (t1 == 0) {
    return 0;
  }


  for(;;) {
    op = ParserToken.getToken(in);
    if (op != T_PLUS && op != T_MINUS) {
      ParserToken.pushbackToken(op);
      return t1;
    }

    ParseTree *t2 = Expr(in);
    if (t2 == 0) {
      return 0;
    }

    // combine t1 and t2 together
    if (op == T_PLUS) {
      ++plCount;
      t1 = new Addition(op.GetLinenum(), t1, t2);
    }
    else
      t1 = new Subtraction(op.GetLinenum(), t1, t2);
  }
  return 0;
}

ParseTree* Term(istream* in) {
  ParseTree *p1 = Primary(in);
  if (p1 == 0) {
    return 0;
  }

  for(;;) {
    Token op = ParserToken.getToken(in);
    if (op != T_STAR && op != T_SLASH) {
      ParserToken.pushbackToken(op);
      return p1;
    }

    ParseTree *p2 = Primary(in);
    if (p2 == 0) {
      error(op.GetLinenum(), "error: term required after * or / operator");
      return 0;
    }


    // combine p1 and p2 together
    if (op == T_STAR) {
      ++multCount;
      p1 = new Multiplication(op.GetLinenum(), p1, p2);
    } else {
      p1 = new Division(op.GetLinenum(), p1, p2);
    }
  }
  // should never get here...
  return 0;
}

ParseTree* Primary(istream* in) {
  Token tk = ParserToken.getToken(in);
  if (tk.GetTokenType() == T_ICONST) {
    return new IntegerConstant(tk);
  } else if (tk.GetTokenType() == T_SCONST) {
    return new StringConstant(tk);
  } else if (tk.GetTokenType() == T_ID) {
    Identifier *identifier = identifierMap[tk.GetLexeme()];
    if (identifier == NULL) {
      error(tk.GetLinenum(), "variable " +tk.GetLexeme()+ " is used before being declared");
      return new ParseTree(tk.GetLinenum(), NULL, NULL);
    }
    return identifier;
  } else if (tk == T_LPAREN) {
    ParseTree *expr = Expr(in);
    if (expr == NULL) {
      error(tk.GetLinenum(), "error: expression was invalid");
      return 0;
    }

    tk = ParserToken.getToken(in);
    if (tk == T_RPAREN) {
      return expr;
    }
  }

  // fall through case where token was read but no match
  ParserToken.pushbackToken(tk);
  error(tk.GetLinenum(), "Syntax error primary expected");
  return 0;
}




