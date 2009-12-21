
; mail/news parms and loader
;
; uses /tlc2

(ns main
    (:refer-clojure)
    (:require tlc)
    )

;load other tlc libs...

(refer 'tlc) ;tlc2

(def mail-folder
     "//server/c/Program Files/Code-Crafters/Ability Mail Server 2/config/accounts/leftofdayton.net/news/"
     )

(def *To "news2@leftofdayton.net")

(def *m$ nil)
(def *i nil)

(def *b-list '(
      *header nil   ;built by "parser.clj"
      *html nil
      *plain nil
      *plain-newline "\r\n"
      *boundary nil
      *parts nil
      *subject nil
      *msg-id nil
      *mime-blurb nil
      *posted-by nil
      *news-server "news@leftofdayton.net"
      *website "http://www.leftofdayton.net/news"
      *newmail nil
      ) )

(eval (cons 'def* *b-list))

(declare *multi *boundary *recdl *posted-by *subj *newsid *ctol)

(ldf? "xmlutil")      ; use xright for html parsing...
(ldf? "udb")          ; the user db as a map, used by subjcmd.clj

(ldf? "parser")

(ldf? "subjcmd.clj")
(ldf? "assemble.clj")   ; assemble and forward the new message
(ldf? "forward")        ; forward the msg

(defn monitor [] ; monitor []
   (let [ dirl (dir (str mail-folder "Inbox/"))
          dirl (filter #(ss % ".txt") dirl)
          ]
(prn dirl)
(brk 'dirl)
        (mapc parse dirl)
        )
   )

(defn new-boundary []
   (str "------" (now) "leftofdayton.net")
   )

(defn go []
  (eval (list 'binding (apply vector *b-list)
          (set! *boundary (new-boundary))
(brk *boundary)
    (parse "plain_text.txt")
 ; (parse "lexplain.txt")
  ; (parse "lexmulti.txt")
 ; (parse "leftmulti.txt")
   (or (subjcmd)
       (do (def *newmail (assemble))
           (forward)
           ) )

      ) ) )

(defn lgo [] (ll) (go))

(println "\n\nMAIN LOADED\n\n")


