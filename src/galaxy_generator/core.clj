(ns galaxy-generator.core
  (:require
    [galaxy-generator.random :as r]
    [quil.core :as q]
    [quil.middleware :as m]))

(defn sphere-stars
  [{:keys [size density
           deviation-x deviation-y deviation-z]
    :or {density 0.000025
         deviation-x (/ size 10)
         deviation-y (/ size 10)
         deviation-z (/ size 10)}}]
  (let [r (java.util.Random.)
        density (max 0 (r/normally-distributed-single r 0 density))
        star-count-max (max 0 (int (* size size size density)))]
    (repeatedly
      star-count-max
      (fn generate-star []
        {:x (r/normally-distributed-single r deviation-x 0)
         :y (r/normally-distributed-single r deviation-y 0)
         :z (r/normally-distributed-single r deviation-z 0)}))))


(defn setup
  []
  {:stars
   (concat
     (map
       (partial merge-with + {:x -200 :y -70})
       (sphere-stars {:size 1000 :density 0.000008}))
     (map
       (partial merge-with + {:x 200 :y -200})
       (sphere-stars {:size 1000 :density 0.000005}))
     (sphere-stars {:size 2000 :density 0.0000007}))
   :rotation-angle 0
   :rotation-speed 0.005})


(defn update-state
  [{:keys [stars rotation-angle rotation-speed] :as state}]
  (update state :rotation-angle
          #(-> % (+ rotation-speed) (mod (* 2 Math/PI)))))


(defn draw-state
  [{:keys [stars rotation-angle] :as state}]
  (q/frame-rate 15)
  (q/background 0)
  (q/stroke 255)
  (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
    (q/with-rotation [rotation-angle 0 -1 0]
      (doseq [{:keys [x y z]} stars]
        (q/point x y z)))))


(q/defsketch galaxy-generator
  :size [1024 1024]
  :renderer :p3d
  :setup setup
  :draw draw-state
  :update update-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
