

; subj handlers

(defn set-zipcode [] (brk 'undefined))
(defn spam [] (brk 'undefined))
(defn like-this [m-or-f] (brk 'undefined))
(defn private-reply [] (brk 'undefined))
(defn poster [m-or-f] (brk 'undefined))
(defn mile-radius [] (brk 'undefined))
(defn join-listserv [flag] (brk 'undefined))


(defn headline [$]
   (let [ a (ss+ *header $) ]
        (substr *header a (ss *header "\r\n"))
        ) )

(defn def-header-parms []
  (def* *posted-by (headline "From: ")
        *subject (headline "Subject: ")
        ) ) 

(defn subjcmd []
  (let [ a (ss+ *header "Subject: ")
         x (def *subject (substr *header a
                           (ss *header "\r\n" a)
                           ) )
         lsubj (trim-white (lower *subject))
         ]
       (def-header-parms)  
       (cond (= lsubj "more like this")
                 (like-this 'more)
             (= lsubj "fewer like this")
                 (like-this 'fewer)
             (= lsubj "more like this")
                 (set-zipcode)    
             (= lsubj "this is spam")
                 (spam)    
             (= lsubj "more from poster")
                 (poster 'more)    
             (= lsubj "fewer from poster")
                 (poster 'fewer)    
             (ss lsubj "private reply:")
                 (private-reply)    
             (ss lsubj "mile radius")
                 (mile-radius)    
             (ss lsubj "join listserv")
                 (join-listserv true)
             (ss lsubj "join-listserv")
                 (join-listserv nil)
             )    
       ) )
