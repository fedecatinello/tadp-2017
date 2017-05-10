require_relative 'CaseClassMixin'

module CaseObjectMixin

  include CaseClassMixin

  def initialize
    self.freeze
  end

  def copy
    self
  end

end

module CaseObjectClassMixin

  def attr_accessor(*args)
    throw 'No se pueden definir atributos en un Case Object.'
  end

end