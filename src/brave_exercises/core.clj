(ns brave-exercises.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

; -------------------------------------------------------
; ---------- The BraveClojure Exercises -----------------
; -------------------------------------------------------

; -------------------------------------------------------
; Chapter 3: Do Things: A Clojure Crash Course
; -------------------------------------------------------

; http://www.braveclojure.com/do-things/#Exercises
; -------------------------------------------------------

(defn add-100
  "#2. Write a function that takes a number and adds 100 to it."
  [number]
  (+ 100 number))

; -------------------------------------------------------

(defn dec-maker
  "#3. Write a function, dec-maker, that works exactly like the function inc-maker except with subtraction."
  [number]
  #(- % number))

(def dec9 (dec-maker 9))

; -------------------------------------------------------

(defn mapset
  "#4. Write a function, mapset, that works like map except the return value is a set."
  [func xs]
  (set (map func xs)))

; -------------------------------------------------------

(def asym-alien-body-parts [{:name "head" :size 3}
                            {:name "left-eye" :size 1}
                            {:name "left-ear" :size 1}
                            {:name "mouth" :size 1}
                            {:name "nose" :size 1}
                            {:name "neck" :size 2}
                            {:name "left-shoulder" :size 3}
                            {:name "left-upper-arm" :size 3}
                            {:name "chest" :size 10}
                            {:name "back" :size 10}
                            {:name "left-forearm" :size 3}
                            {:name "abdomen" :size 6}
                            {:name "left-kidney" :size 1}
                            {:name "left-hand" :size 2}
                            {:name "left-knee" :size 2}
                            {:name "left-thigh" :size 4}
                            {:name "left-lower-leg" :size 3}
                            {:name "left-achilles" :size 1}
                            {:name "left-foot" :size 2}])

(defn matching-alien-part
  [part suffix]
  [{:name (clojure.string/replace (:name part) #"^left-" (str suffix "-"))
   :size (:size part)}])

(defn several-sides
  [part total]
  (if (= total 1)
    part
    (several-sides (into part (matching-alien-part (first part) (str total))) (dec total))))

(defn symmetrize-alien-body-parts
  "#5. Create a function that’s similar to symmetrize-body-parts except that
  it has to work with weird space aliens with radial symmetry. Instead of two eyes,
  arms, legs, and so on, they have five."
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set (several-sides [part] 5))))
          []
          asym-body-parts))

; -------------------------------------------------------

(defn general-symmetrize-body-parts
  "#6. The new function should take a collection of body parts and the number of matching body parts to add."
  [asym-body-parts sides]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set (several-sides [part] sides))))
          []
          asym-body-parts))

; -------------------------------------------------------
; Chapter 4: Do Things: Core Functions in Depth
; -------------------------------------------------------

; http://www.braveclojure.com/core-functions-in-depth/#reduce
; -------------------------------------------------------

(defn to-list
  "#1. Turn the result of your glitter filter into a list of names."
  [data]
  (map #(:name %) data))

; -------------------------------------------------------

(defn my-append
  "#2. Write a function which will append a new suspect to your list of suspects."
  [new-suspect suspect-list]
  ; append at first place
  ; (into suspect-list [(:name new-suspect)]))
  ; append at the end
  (concat (into [] suspect-list) [(:name new-suspect)]))

; -------------------------------------------------------

(defn validate
  "#3.  Write a function which will check that :name and :glitter-index are present when you append.
   The validate function should accept two arguments: a map of keywords to validating functions,
   similar to conversions, and the record to be validated."
  [keywords new-suspect]
  (if (empty? keywords)
    true
    (and ((first keywords) new-suspect) (validate (rest keywords) new-suspect))))

; -------------------------------------------------------

(def vamp-filename "vampires.csv")

(defn vampire-row
  [vampire-data]
  (str (reduce (fn [res vamp-key]
                 (str res (vamp-key vampire-data) ","))
                ""
                (drop-last vamp-keys))
       ((last vamp-keys) vampire-data)
       "\n"))

(defn vampire-row-join
  [vampire-data]
  (str (reduce (fn [res vamp-key]
                 (if (empty? res)
                   (vamp-key vampire-data)
                   (clojure.string/join "," [res (vamp-key vampire-data)])))
                ""
                vamp-keys)
       "\n"))

(defn store
  "#4.  Write a function that will take your list of maps and convert it back to a CSV string.
   You’ll need to use the clojure.string/join function."
  [vampires-list]
  (spit vamp-filename (reduce (fn [res vampire-data]
                                (str res (vampire-row-join vampire-data)))
                              ""
                              vampires-list)))

; -------------------------------------------------------
